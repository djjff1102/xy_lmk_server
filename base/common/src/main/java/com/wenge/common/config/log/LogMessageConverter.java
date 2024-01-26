package com.wenge.common.config.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.wenge.common.constants.Constants;

/**
 * @author HAO灏 2021/1/5 16:39
 */
public class LogMessageConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		return "[trace_id:" + Constants.TRACE_ID.get() + "] ";
	}

}
