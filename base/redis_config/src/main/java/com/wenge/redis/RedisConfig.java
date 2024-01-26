package com.wenge.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author HAO灏 2020/11/17 19:38
 */
@Configuration
public class RedisConfig {


	@Bean("redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Bean("listOperations")
	public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForList();
	}

	@Bean("stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
		return stringRedisTemplate;
	}

	@Bean("stringValueOperations")
	public ValueOperations<String, String> stringValueOperations(StringRedisTemplate stringRedisTemplate) {
		return stringRedisTemplate.opsForValue();
	}

}
