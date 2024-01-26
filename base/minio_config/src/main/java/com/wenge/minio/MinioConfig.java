package com.wenge.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author ziqian
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

	private String endpoint;
	private String accessKey;
	private String secretKey;

	private String bucket;


	@Bean
	public MinioClient minioClient() throws Exception {
		MinioClient minioClient = MinioClient.builder()
				.endpoint(this.endpoint)
				.credentials(this.accessKey, this.secretKey)
				.build();
		boolean exist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(this.bucket).build());
		if (!exist) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(this.bucket).build());
		}
		return minioClient;
	}

}
