package com.wenge.xxl_job;

import com.xxl.job.core.biz.model.ReturnT;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.net.URI;
import java.util.Map;

/**
 * @author HAO灏 2022/11/23 20:25
 */
public interface XxlJobService {

	@RequestLine("POST /jobinfo/start?id={id}")
	@Headers({"Cookie:{cookie}"})
	ReturnT<?> start(URI baseUri, @Param("cookie") String cookie, @Param("id") int id);

	@RequestLine("POST /jobinfo/add")
	@Headers({"Cookie:{cookie}"})
	ReturnT<?> addJobInfo(URI baseUri, @Param("cookie") String cookie, @QueryMap XxlJobInfo xxlJobInfo);


	@RequestLine("GET /jobgroup/pageList")
	@Headers("Cookie:{cookie}")
	Map<String, Object> getGroup(
			URI baseUri, @Param("cookie") String cookie, @QueryMap Map<String, Object> query);

	@RequestLine("POST /jobinfo/update")
	@Headers({"Cookie:{cookie}"})
	ReturnT<?> updateJobInfo(URI baseUri, @Param("cookie") String cookie, @QueryMap XxlJobInfo xxlJobInfo);

	@RequestLine("GET /jobinfo/pageList")
	@Headers({"Cookie:{cookie}"})
	Map<String, Object> getJobInfo(
			URI baseUri, @Param("cookie") String cookie, @QueryMap Map<String, Object> query);

	@RequestLine("POST /jobinfo/remove?id={id}")
	@Headers({"Cookie:{cookie}"})
	ReturnT<?> removeJobInfo(URI baseUri, @Param("cookie") String cookie, @Param("id") int id);

	@RequestLine("POST /jobinfo/stop?id={id}")
	@Headers({"Cookie:{cookie}"})
	ReturnT<?> stop(URI baseUri, @Param("cookie") String cookie, @Param("id") int id);

}
