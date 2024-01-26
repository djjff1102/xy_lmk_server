package com.wenge.common.util;

import com.wenge.common.config.log.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zw
 * @date 2020/2/16 23:38
 */
public class SHA256Util {

	public static String getSHA256StrJava(String str) {

		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			Log.logger.error(e.getMessage(), e);
		}
		return encodeStr;
	}

	private static String byte2Hex(byte[] bytes) {
		StringBuilder stringBuffer = new StringBuilder();
		String temp = null;
		for (byte aByte : bytes) {
			temp = Integer.toHexString(aByte & 0xFF);
			if (temp.length() == 1) {
				//1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
}
