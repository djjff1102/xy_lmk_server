//package com.wenge.elasticsearch;
//
//import com.wenge.common.config.log.Log;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
//import org.springframework.data.elasticsearch.core.document.SearchDocumentResponse;
//import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
//import org.springframework.data.elasticsearch.core.query.Query;
//import org.springframework.util.Assert;
//import org.springframework.util.ReflectionUtils;
//
//import java.io.IOException;
//import java.lang.reflect.Method;
//
//
//public class LogDSLElasticsearchRestTemplate extends ElasticsearchRestTemplate {
//
//	public LogDSLElasticsearchRestTemplate(RestHighLevelClient client) {
//		super(client);
//	}
//
//	private RestHighLevelClient client;
//
//	public LogDSLElasticsearchRestTemplate(RestHighLevelClient client, ElasticsearchConverter elasticsearchConverter) {
//		super(client, elasticsearchConverter);
//		this.init();
//		this.client = client;
//	}
//
//	private Method searchRequestMethod;
//
//	private void init() {
//		Method searchRequestMethod = null;
//		try {
//			searchRequestMethod = ReflectionUtils.findMethod(Class.forName("org.springframework.data.elasticsearch.core.RequestFactory"), "searchRequest", Query.class, Class.class, IndexCoordinates.class);
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException(e);
//		}
//		searchRequestMethod.setAccessible(true);
//		this.searchRequestMethod = searchRequestMethod;
//	}
//
//	@Override
//	public <T> SearchHits<T> search(Query query, Class<T> clazz, IndexCoordinates index) {
//		try {
//
//			Object o = this.searchRequestMethod.invoke(this.requestFactory, query, clazz, index);
//
//			//Field source = ReflectionUtils.findField(Class.forName("org.elasticsearch.action.search.SearchRequest"), "source");
//			//source.setAccessible(true);
//			//Object s = ReflectionUtils.getField(source, o);
//
//
//			//SearchRequest searchRequest = requestFactory.searchRequest(query, clazz, index);
//			SearchRequest searchRequest = (SearchRequest) o;
//			Log.logger.info("dsl:{}", searchRequest.source().toString());
//			SearchResponse response = execute(new ClientCallback<SearchResponse>() {
//				@Override
//				public SearchResponse doWithClient(RestHighLevelClient client) throws IOException {
//					return client.search(searchRequest, RequestOptions.DEFAULT);
//				}
//			});
//
//			SearchDocumentResponseCallback<SearchHits<T>> callback = new ReadSearchDocumentResponseCallback<>(clazz, index);
//			return callback.doWith(SearchDocumentResponse.from(response));
//		} catch (Exception e) {
//			Log.logger.error(e.getMessage(), e);
//			return null;
//		}
//
//	}
//
//	@Override
//	public <T> T execute(ClientCallback<T> callback) {
//
//		Assert.notNull(callback, "callback must not be null");
//
//		try {
//			return callback.doWithClient(this.client);
//		} catch (IOException | RuntimeException e) {
//			//throw translateException(e);
//			return null;
//		}
//	}
//
//}
