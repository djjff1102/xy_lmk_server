//package com.wenge.nacos;
//
//import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
//import com.alibaba.cloud.nacos.NacosServiceManager;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.NamingService;
//import com.alibaba.nacos.api.naming.listener.NamingEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//
//@Component
//public class NacosServerListListener implements ServerListListener {
//
//	private static final Logger logger = LoggerFactory.getLogger(NacosServerListListener.class);
//
//	@Resource
//	private NacosServiceManager nacosServiceManager;
//
//	private NamingService namingService;
//
//	@Resource
//	private NacosDiscoveryProperties properties;
//
//	@PostConstruct
//	public void init() {
//		nacosServiceManager.setNacosDiscoveryProperties(properties);
//		namingService = nacosServiceManager.getNamingService();
//	}
//
//	/**
//	 * 创建监听器
//	 */
//	@Override
//	public void listen(String serviceId, ServerEventHandler eventHandler) {
//		try {
//			namingService.subscribe(serviceId, event -> {
//				if (event instanceof NamingEvent) {
//					NamingEvent namingEvent = (NamingEvent) event;
//					logger.info("服务名：" + namingEvent.getServiceName());
//					logger.info("实例：" + namingEvent.getInstances());
//					// 实际更新
//					eventHandler.update();
//				}
//			});
//		} catch (NacosException e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
//}
