package com.wenge.elasticsearch;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.Map;

/**
 * @author HAO灏 2020/10/20 19:16
 */
public interface ElasticSearchFeignService {


	@RequestLine("POST /{index}/_search")
	Map<String, Object> dslSearch(URI uri, @Param("index") String index, Map<String, Object> dsl);

	@RequestLine("POST /_nlpcn/sql")
	@Headers("Content-type: " + MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> sqlSearch(URI uri, String sql);

	@RequestLine("POST /_nlpcn/sql/explain")
	Map<String, Object> sql2dsl(URI uri, String sql);

}
