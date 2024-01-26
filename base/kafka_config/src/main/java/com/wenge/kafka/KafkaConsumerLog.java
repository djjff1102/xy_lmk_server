package com.wenge.kafka;


import com.wenge.common.config.log.Log;
import com.wenge.common.constants.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author HAO灏 2021/5/6 14:06
 */
@Component
@Aspect
public class KafkaConsumerLog {

	@Before("@annotation(kafkaListener)")
	public void before(JoinPoint joinPoint, KafkaListener kafkaListener) {
		Log.logger.info("Kafka topic:{} Start:{}", kafkaListener.topics(), joinPoint.getArgs());
	}

	@After("@annotation(kafkaListener)")
	public void after(KafkaListener kafkaListener) {
		Log.logger.info("Kafka topic:{} group:{} End", kafkaListener.topics(), kafkaListener.groupId());
		Constants.removeAllThreadLocal();
	}
}
