package com.wenge.common.config.feign;

import com.wenge.common.config.log.Log;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author HAO灏 2020/9/30 17:07
 */
@Configuration
public class FeignConfig {

	@Resource
	private HttpMessageConverter<Object> httpMessageConverter;

	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	public Request.Options feignOptions() {
		return new Request.Options(30, TimeUnit.SECONDS, 20 * 60, TimeUnit.SECONDS, true);
	}

	@Bean
	public Logger feignLogger() {
		return new Logger() {
			@Override
			protected void log(String configKey, String format, Object... args) {
				Log.logger.info(String.format(methodTag(configKey) + format, args));
			}
		};
	}

	@Bean
	public Decoder feignDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers) {
		return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.feignHttpMessageConverter(), customizers)));
	}

	@Bean
	public Encoder feignEncoder() {
		return new SpringEncoder(this.feignHttpMessageConverter());
	}

	private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
		return () -> new HttpMessageConverters(this.httpMessageConverter);
	}

	@Bean
	public Retryer feignRetry() {
		return Retryer.NEVER_RETRY;
	}


}
