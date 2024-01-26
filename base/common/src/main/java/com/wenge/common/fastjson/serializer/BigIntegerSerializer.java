package com.wenge.common.fastjson.serializer;

import java.math.BigInteger;

/**
 * @author HAO灏 2021/2/8 12:03
 */
public class BigIntegerSerializer extends BaseStringSerializer<BigInteger> {

	public static final BigIntegerSerializer instance = new BigIntegerSerializer();

	@Override
	public String apply(BigInteger b) {
		return b.toString();
	}

}
