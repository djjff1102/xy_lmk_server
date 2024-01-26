package com.wenge.common.config.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.wenge.common.util.CommonUtil;

/**
 * @author HAO灏 2021/1/5 16:39
 */
public class AppInfoConverter extends ClassicConverter {

	public static String springApplicationName;
	public static String springCloudClientIpAddress;

	@Override
	public String convert(ILoggingEvent event) {
		if (CommonUtil.isBlank(springApplicationName) || CommonUtil.isBlank(springCloudClientIpAddress)) {
			return "";
		}
		return "<" + springApplicationName + "@" + springCloudClientIpAddress + "> ";
	}

}
