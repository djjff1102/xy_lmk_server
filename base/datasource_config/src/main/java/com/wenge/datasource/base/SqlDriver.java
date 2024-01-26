package com.wenge.datasource.base;

import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HAO灏 2022/12/8 23:40
 */
public class SqlDriver extends MybatisXMLLanguageDriver {


	private final Pattern inPattern = Pattern.compile("\\(#\\{([\\w.]+)}\\)");

	@Override
	public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
		Matcher matcher = inPattern.matcher(script);
		if (matcher.find()) {
			script = script.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			matcher.reset(script);
			script = matcher.replaceAll("(<foreach collection=\"$1\" item=\"item\" separator=\",\" >#{item}</foreach>)");
			script = "<script>" + script + "</script>";
		}
		return super.createSqlSource(configuration, script, parameterType);
	}

}
