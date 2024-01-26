package com.wenge.common.config.mapstruct;

import com.wenge.common.util.CommonUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * @author HAO灏 2020/8/24 13:56
 */
public class DateConverter {

	public String Date2String(Date date) {
		return CommonUtil.formatDate(date);
	}

	public Date String2Date(String date) throws ParseException {
		return CommonUtil.parseDate(date);
	}

}
