package com.wenge.elasticsearch;

import com.wenge.common.config.feign.BaseFeignClientConfig;
import com.wenge.common.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.elasticsearch")
public class ElasticSearchConfig extends BaseFeignClientConfig {

	@Bean
	public ElasticSearchFeignService elasticSearchService() throws Exception {
		return this.createFeignClient0(ElasticSearchFeignService.class);
	}

	private List<URI> elasticSearchUris;
	private int elasticSearchSize;

	public URI getElasticSearchUri() {
		return elasticSearchUris.get(CommonUtil.toInt(CommonUtil.randomNum(4)) % this.elasticSearchSize);
	}

	public void setUris(List<String> uris) {
		this.elasticSearchSize = uris.size();
		this.elasticSearchUris = uris.stream().map(URI::create).collect(Collectors.toList());
	}
}
