package com.wenge.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringTools {

	/**
	 * 处理替换 SQL语句 中的关键字
	 *
	 * @param sqlValue
	 * @return
	 */
	public static String replaceSQLKey(String sqlValue) {
		return sqlValue.replaceAll("_", "\\\\_").replaceAll("%", "\\\\%").replaceAll("'", "''");
	}

	/**
	 * 处理特殊字符
	 *
	 * @param strValue
	 * @return
	 */
	public static String replaceIllegal(String strValue) {
		return strValue.replaceAll(" · ", "").trim();
	}

	/**
	 * 替换字符串中的中文
	 *
	 * @param strValue
	 * @param replacement
	 * @return
	 */
	public static String replaceChinese(String strValue, String replacement) {
		strValue = replaceIllegal(strValue);
		String REGEX_CHINESE = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(REGEX_CHINESE);
		Matcher mat = pat.matcher(strValue);
		return mat.replaceAll(replacement);
	}

	public static List<String> convertToList(String str, String sep) {
		if(CommonUtil.isNotBlank(str)) {
			String[] items = str.split(sep);
			return Arrays.asList(items);
		}
		return new ArrayList<>();
	}
}
