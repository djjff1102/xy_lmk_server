package com.wenge.xxl_job;

import com.wenge.common.config.feign.BaseFeignClientConfig;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * xxl-job config
 */
@Getter
@Setter
@Component
@ConfigurationProperties("quesoar.xxl.job")
public class XxlJobConfig extends BaseFeignClientConfig {
	private String address = "";
	private String ip = "";
	private int port = 0;
	private String logPath = "";
	private String adminAddresses = "";
	private String accessToken = "";
	private String appName = "";

	private int logRetentionDays = 0;
	private String token = "";

	@Bean
	public XxlJobSpringExecutor xxlJobExecutor() {
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(this.adminAddresses);
		xxlJobSpringExecutor.setAppname(this.appName);
		xxlJobSpringExecutor.setAddress(this.address);
		xxlJobSpringExecutor.setLogPath(this.logPath);
		xxlJobSpringExecutor.setLogRetentionDays(this.logRetentionDays);
		xxlJobSpringExecutor.setIp(this.ip);
		xxlJobSpringExecutor.setPort(this.port);
		xxlJobSpringExecutor.setAccessToken(this.accessToken);
		return xxlJobSpringExecutor;
	}

	@Bean
	public XxlJobService xxlJobService() throws Exception {
		return this.createFeignClient(XxlJobService.class);
	}

	/*
	  针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；

	       1、引入依赖：
	           <dependency>
	              <groupId>org.springframework.cloud</groupId>
	              <artifactId>spring-cloud-commons</artifactId>
	              <version>${version}</version>
	          </dependency>

	       2、配置文件，或者容器启动变量
	           spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'

	       3、获取IP
	           String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
	 */


}
