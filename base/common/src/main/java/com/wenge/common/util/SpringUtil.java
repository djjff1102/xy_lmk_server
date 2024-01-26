package com.wenge.common.util;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author HAO灏 2020/9/24 11:42
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public SpringUtil() {
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public static <T> T getBean(Class<T> beanClass) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanClass);
	}

	public static <T> T getBean(String beanName, Class<T> beanClass) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanName, beanClass);
	}

	public static Object proxy() {
		return AopContext.currentProxy();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}
}
