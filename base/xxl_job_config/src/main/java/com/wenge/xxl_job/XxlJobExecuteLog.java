package com.wenge.xxl_job;


import com.wenge.common.config.log.Log;
import com.wenge.common.constants.Constants;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author HAO灏 2021/5/6 14:06
 */
@Component
@Aspect
public class XxlJobExecuteLog {

	@Before("@annotation(xxlJob)")
	public void before(XxlJob xxlJob) {
		Log.logger.info("XxlJob--{}--START", xxlJob.value());
	}

	@After("@annotation(xxlJob)")
	public void after(XxlJob xxlJob) {
		Log.logger.info("XxlJob--{}--END", xxlJob.value());
		Constants.removeAllThreadLocal();
	}

}
