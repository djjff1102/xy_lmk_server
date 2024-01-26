package com.wenge.common.fastjson.serializer;


import com.wenge.common.util.MessageUtil;

/**
 * @author HAO灏 2020/8/26 20:53
 */
public class MessageSourceSerializer extends BaseStringSerializer<String> {

	@Override
	public String apply(String message) {
		return MessageUtil.getMessage(message);
	}

}
