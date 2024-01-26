package com.wenge.tdxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = "com.wenge")
@ServletComponentScan
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
//@EnableLoggingClient
public class XyLmkBusinessApplication {

	public static void main(String[] args) {

		SpringApplication.run(XyLmkBusinessApplication.class, args);
	}

}
