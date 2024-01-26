package com.wenge.common.fastjson.serializer;


import com.wenge.common.util.CommonUtil;

import java.util.Date;

/**
 * @author HAO灏 2021/2/8 12:03
 */
public class DateSerializer extends BaseStringSerializer<Date> {

	public static final DateSerializer instance = new DateSerializer();

	@Override
	public String apply(Date date) {
		return CommonUtil.formatDate(date);
	}

}
