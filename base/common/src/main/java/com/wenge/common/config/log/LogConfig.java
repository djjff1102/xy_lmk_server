package com.wenge.common.config.log;

import ch.qos.logback.classic.PatternLayout;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

/**
 * @author HAO灏 2020/12/15 09:26
 */
@Component
public class LogConfig implements GenericApplicationListener {

	@Override
	public boolean supportsEventType(@NotNull ResolvableType resolvableType) {
		return true;
	}


	@Override
	public void onApplicationEvent(@NotNull ApplicationEvent applicationEvent) {
		PatternLayout.DEFAULT_CONVERTER_MAP.put("trace_id", LogMessageConverter.class.getName());
		PatternLayout.DEFAULT_CONVERTER_MAP.put("app_info", AppInfoConverter.class.getName());
	}

	@Value("${spring.application.name}")
	public void setSpringApplicationName(String springApplicationName) {
		AppInfoConverter.springApplicationName = springApplicationName;
	}

	@Value("${spring.cloud.client.ip-address}")
	public void setSpringCloudClientIpAddress(String springCloudClientIpAddress) {
		AppInfoConverter.springCloudClientIpAddress = springCloudClientIpAddress;
	}

}
