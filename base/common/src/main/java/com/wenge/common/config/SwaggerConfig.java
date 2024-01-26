//package com.wenge.common.config;
//
//import com.wenge.common.util.CommonUtil;
//import io.swagger.annotations.ApiOperation;
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// * @author HAO灏 2020/8/13 16:37
// */
//@Data
//@Component
//@RefreshScope
//@EnableOpenApi
//@ConfigurationProperties("swagger.config")
//public class SwaggerConfig {
//
//	private String title;
//
//	@Bean
//	public Docket createRestApi() {
//		boolean enable = CommonUtil.isNotBlank(this.title);
//		return new Docket(DocumentationType.OAS_30)
//				.enable(enable)
//				.apiInfo(this.apiInfo())
//				.select()
//				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//				.paths(PathSelectors.any())
//				.build();
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder()
//				.title(this.title)
//				.version("1.0")
//				.build();
//	}
//}
