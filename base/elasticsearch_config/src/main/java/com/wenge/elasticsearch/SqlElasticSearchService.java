package com.wenge.elasticsearch;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wenge.common.result.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SqlElasticSearchService {

	@Resource
	private ElasticSearchFeignService elasticSearchFeignService;
	@Resource
	private ElasticSearchConfig elasticSearchConfig;

	public PageResult<JSONObject> sqlSearch(String sql) {
		JSONObject esResult = new JSONObject(elasticSearchFeignService.sqlSearch(elasticSearchConfig.getElasticSearchUri(), sql));
		JSONObject hits = esResult.getJSONObject("hits");
		JSONArray data = hits.getJSONArray("hits");
		long total = hits.getJSONObject("total").getLong("value");
		return PageResult.success(data.parallelStream().map(map -> ((JSONObject) map).getJSONObject("_source")).collect(Collectors.toList()), total);
	}

	public List<JSONObject> sqlSearch0(String sql) {
		JSONObject esResult = new JSONObject(elasticSearchFeignService.sqlSearch(elasticSearchConfig.getElasticSearchUri(), sql));
		JSONObject hits = esResult.getJSONObject("hits");
		JSONArray data = hits.getJSONArray("hits");
		return data.parallelStream().map(map -> ((JSONObject) map).getJSONObject("_source")).collect(Collectors.toList());
	}


}
