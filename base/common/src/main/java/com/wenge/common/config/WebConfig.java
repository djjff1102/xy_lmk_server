package com.wenge.common.config;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.wenge.common.fastjson.serializer.BigIntegerSerializer;
import com.wenge.common.fastjson.serializer.DateSerializer;
import com.wenge.common.fastjson.serializer.LongSerializer;
import com.wenge.common.util.CommonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**")
				.allowedHeaders("*")
				.allowedMethods("POST", "GET", "PATCH", "DELETE", "PUT")
				.allowedOrigins("*");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.clear();
		converters.add(this.httpMessageConverter());
	}

	@Bean
	public HttpMessageConverter<Object> httpMessageConverter() {
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		fastJsonHttpMessageConverter.setSupportedMediaTypes(CommonUtil.ofList(MediaType.APPLICATION_JSON,
				MediaType.TEXT_PLAIN, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_OCTET_STREAM));
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setWriterFilters(ValueFilter.INSTANCE);
		fastJsonConfig.setReaderFilters(ValueFilter.INSTANCE);
		//BigDecimal使用plain，防止转换为科学计数法0E-8
		fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteBigDecimalAsPlain);
		//fastJsonConfig.setReaderFeatures(JSONReader.Feature.SupportSmartMatch);
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		this.initSerializeConfig();
		return fastJsonHttpMessageConverter;
	}

	private void initSerializeConfig() {
		JSONFactory.getDefaultObjectWriterProvider().register(Long.class, LongSerializer.instance);
		JSONFactory.getDefaultObjectWriterProvider().register(Date.class, DateSerializer.instance);
		JSONFactory.getDefaultObjectWriterProvider().register(BigInteger.class, BigIntegerSerializer.instance);
	}


}
