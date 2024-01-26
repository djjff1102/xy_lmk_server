package com.wenge.common.config.feign;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import java.util.concurrent.TimeUnit;

@Configuration
public class BaseFeignClientConfig {
	@Resource
	private Decoder decoder;
	@Resource
	private Encoder encoder;
	@Resource
	private Logger logger;
	@Resource
	private Logger.Level logLevel;
	@Resource
	private Retryer retryer;
	@Resource
	private FeignErrorDecoder errorDecoder;
	@Resource
	private FeignRequestInterceptor interceptor;

	private <T> T createFeignClient(Class<T> clazz, Request.Options requestOptions, boolean encoderFlag) throws Exception {
		//信任https证书
		SSLContext context = new SSLContextBuilder()
				.loadTrustMaterial(null, (chain, authType) -> true)
				.build();
		Feign.Builder builder = Feign.builder()
				.client(new Client.Default(context.getSocketFactory(), new NoopHostnameVerifier()))
				.options(requestOptions)
				.logLevel(logLevel)
				.retryer(retryer)
				.decoder(decoder)
				.logger(logger)
				.errorDecoder(errorDecoder)
				.requestInterceptor(interceptor);
		if (encoderFlag) {
			builder.encoder(encoder);
		}
		return builder.target(Target.EmptyTarget.create(clazz));
	}

	public <T> T createFeignClient(Class<T> clazz) throws Exception {
		return this.createFeignClient(clazz, 30, 20 * 60, true);
	}

	public <T> T createFeignClient(Class<T> clazz, int connectTimeoutSeconds, int readTimeoutSeconds, boolean encoderFlag) throws Exception {
		return this.createFeignClient(clazz, new Request.Options(connectTimeoutSeconds, TimeUnit.SECONDS,
				readTimeoutSeconds, TimeUnit.SECONDS, true), encoderFlag);
	}

	public <T> T createFeignClient0(Class<T> clazz) throws Exception {
		return this.createFeignClient(clazz, 30, 20 * 60, false);
	}

}
