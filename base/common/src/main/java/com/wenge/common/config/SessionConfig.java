package com.wenge.common.config;

import com.alibaba.fastjson2.support.spring.data.redis.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;


/**
 * @author HAO灏 2020/9/10 15:05
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {

	@Bean("springSessionDefaultRedisSerializer")
	public RedisSerializer<Object> defaultRedisSerializer() {
		return new FastJsonRedisSerializer<>(Object.class);
	}

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
		cookieSerializer.setSameSite(null);
		cookieSerializer.setUseHttpOnlyCookie(true);
		cookieSerializer.setCookiePath("/");
		return cookieSerializer;
	}
}


