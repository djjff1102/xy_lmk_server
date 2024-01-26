package com.wenge.common.util;


import com.wenge.common.config.log.Log;
import com.wenge.common.constants.Constants;
import org.springframework.context.MessageSource;

public class MessageUtil {

	private static final MessageSource messageSource;

	static {
		messageSource = SpringUtil.getBean(MessageSource.class);
	}

	public static String getMessage(String message) {
		return getMessage(message, null);
	}

	public static String getMessage(String message, String[] args) {
		try {
			if (messageSource == null) {
				return message;
			}
			if (CommonUtil.isBlank(message)) {
				return "";
			}
			message = messageSource.getMessage(message, args, Constants.CURRENT_LOCALE.get());
		} catch (Exception e) {
			Log.logger.warn(e.getMessage());
		}
		return message;
	}

}
