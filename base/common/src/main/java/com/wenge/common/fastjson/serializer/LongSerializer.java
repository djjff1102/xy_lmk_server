package com.wenge.common.fastjson.serializer;

/**
 * @author HAO灏 2021/2/8 12:03
 */
public class LongSerializer extends BaseStringSerializer<Long> {

	public static final LongSerializer instance = new LongSerializer();

	@Override
	public String apply(Long l) {
		return l + "";
	}

}
