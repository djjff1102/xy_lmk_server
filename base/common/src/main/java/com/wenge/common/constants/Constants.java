package com.wenge.common.constants;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.lang.NonNull;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * @author HAO灏 2020/8/7 09:54
 */
public class Constants {

	public final static TransmittableThreadLocal<Locale> CURRENT_LOCALE = new TransmittableThreadLocal<>();

	@NonNull
	public final static ExecutorService CONCURRENT_THREAD_POOL = TtlExecutors.getTtlExecutorService(new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 4));

	public final static ThreadLocal<String> TRACE_ID = ThreadLocal.withInitial(IdWorker::getIdStr);

	public final static TransmittableThreadLocal<String> REMOTE_REQUEST_IP = new TransmittableThreadLocal<>();

	public final static TransmittableThreadLocal<String> REQUEST_DATA_PERMISSION_CODE = new TransmittableThreadLocal<>();

	public final static long DEFAULT_SYSTEM_ID_MAX = 999999999L;

	public final static String NO_DATA_PERMISSION_STR = "****";
	public static final long MAX_FILE_SIZE = 20 * 1024 * 1024;

	public static void removeAllThreadLocal() {
		CURRENT_LOCALE.remove();
		TRACE_ID.remove();
		REMOTE_REQUEST_IP.remove();
		AuthConstants.remove();
		REQUEST_DATA_PERMISSION_CODE.remove();
	}


	//////InheritableThreadLocal问题测试
	//public static void main(String[] args) throws InterruptedException {
	//	ExecutorService testpool = new ForkJoinPool(1);
	//	TransmittableThreadLocal<Integer> transmittableThreadLocal = new TransmittableThreadLocal<>();
	//	InheritableThreadLocal<Integer> inheritableThreadLocal = new InheritableThreadLocal<>();
	//	transmittableThreadLocal.set(2);
	//	inheritableThreadLocal.set(2);
	//	int i = 0;
	//	while (true) {
	//		CONCURRENT_THREAD_POOL.submit(() -> System.out.println("TransmittableThreadLocal:" + transmittableThreadLocal.get()));
	//		testpool.submit(() -> System.out.println("InheritableThreadLocal:" + inheritableThreadLocal.get()));
	//		TimeUnit.SECONDS.sleep(1L);
	//		i++;
	//		if (i == 3) {
	//			System.out.println("--------set---------");
	//			transmittableThreadLocal.set(3);
	//			inheritableThreadLocal.set(3);
	//		}
	//		if (i == 5) {
	//			System.out.println("--------remove---------");
	//			transmittableThreadLocal.remove();
	//			inheritableThreadLocal.remove();
	//		}
	//	}
	//
	//}

}
