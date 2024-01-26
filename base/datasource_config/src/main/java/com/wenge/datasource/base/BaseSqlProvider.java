package com.wenge.datasource.base;

import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HAO灏 2022/12/6 19:38
 */
public class BaseSqlProvider {

	private static final Pattern pattern = Pattern.compile("[)?| *]((?i)in) *\\( *#\\{.*");

	public String formatSqlIn(String sql, Map<String, Object> params) {
		return formatSqlIn(sql, 0, params);
	}

	private String formatSqlIn(String sql, int beginIndex, Map<String, Object> params) {
		int i = inPosition(sql, beginIndex);
		if (i <= 0) {
			return sql;
		}
		int j = sql.indexOf('(', i);
		if (j > 0) {
			int k = sql.indexOf(')', j + 1);
			if (k > 0) {
				String s = sql.substring(j + 1, k).trim();
				if ((s.startsWith("#{") || s.startsWith("${")) && s.endsWith("}")) {
					String p = s.substring(2, s.length() - 1).trim();
					StringBuilder buf = new StringBuilder(512);
					buf.append(sql, 0, j + 1);
					String[] pp = p.split("\\.");
					Collection<?> values = (Collection<?>) params.get(pp[1]);
					int t = 0;
					StringBuilder subBuf = new StringBuilder(128);
					for (Object value : values) {
						if (t == 0) {
							subBuf.append("#{params.").append(pp[1]).append("_").append(t).append("}");
							params.put(pp[1] + "_" + t, value);
						} else {
							subBuf.append(",#{params.").append(pp[1]).append("_").append(t).append("}");
							params.put(pp[1] + "_" + t, value);
						}
						t++;
					}
					buf.append(subBuf);
					buf.append(sql.substring(k));
					k = j + subBuf.length();
					return formatSqlIn(buf.toString(), k, params);
				}
			}
		}
		return sql;
	}


	private int inPosition(String sql, int beginIndex) {
		int i = -1;
		Matcher m = pattern.matcher(sql);
		if (m.find(beginIndex)) {
			String s = m.group();
			i = sql.indexOf(s, beginIndex);
		}
		return i;
	}

	public String executeSql(String sql, @Param("params") Map<String, Object> params) {
		return formatSqlIn(sql, 0, params);
	}

}
