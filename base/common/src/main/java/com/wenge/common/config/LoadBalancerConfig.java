package com.wenge.common.config;

import com.wenge.common.util.CommonUtil;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author HAO灏 2020/8/20 11:13
 */
@Configuration
public class LoadBalancerConfig {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(HttpMessageConverter<Object> httpMessageConverter) {
		return new RestTemplate(CommonUtil.ofList(httpMessageConverter));
	}

}
