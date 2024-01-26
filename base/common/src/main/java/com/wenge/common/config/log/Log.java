package com.wenge.common.config.log;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author HAO灏 2020/12/14 16:17
 */
//@Async
//@EnableAsync
@Component
@Getter
@Setter
@ConfigurationProperties("logging")
public class Log {

	//统一用这个logger记录日志，方便以后修改
	public final static Logger logger = LoggerFactory.getLogger(Log.class);


}
