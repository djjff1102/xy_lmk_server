package com.wenge.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author HAO灏 2020/10/16 13:17
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("rsa.config")
public class RSAConfig {

	private String public_key;

	private String private_key;
}
