package com.wenge.redis;


import com.wenge.common.config.log.Log;
import com.wenge.common.constants.AuthConstants;
import com.wenge.common.exception.LockedException;
import com.wenge.common.result.Result;
import com.wenge.common.util.CommonUtil;
import com.wenge.common.util.SpringUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author HAO灏 2020/9/10 14:07
 * 加锁，一般不需要扩展此类，此类中全部为锁相关内容
 */
public class Lock implements AutoCloseable {

	private final static RedissonClient redissonClient;

	static {
		redissonClient = SpringUtil.getBean(RedissonClient.class);
	}

	/**
	 * 已经上锁的key
	 */
	private final List<RLock> lockedKeys = new ArrayList<>();
	/**
	 * 如果在new Lock时，tenantId=-1，然后创建了锁，然后在执行的业务代码中set了tenantId，会导致解锁的时候无法解开
	 * 所以需要保存住创建锁的时候的tenantId，解锁的时候，用这个解
	 */
	private final long tenantId = AuthConstants.CURRENT_TENANT_ID.get();
	private final String userName = AuthConstants.getCurrentUser() == null ? "" : AuthConstants.getCurrentUser().getName();

	public Lock() {

	}

	/**
	 * 通过构造函数加锁，将会通过抛出异常的方式终止程序运行
	 */
	public Lock(Object key) throws Exception {
		Result<?> lockResult = this.lock(key);
		if (lockResult.failed()) {
			throw new LockedException(lockResult.getMessage());
		}
	}


	public Lock(Collection<?> keys) throws Exception {
		if (CommonUtil.isEmpty(keys)) {
			return;
		}
		for (Object key : keys) {
			Result<?> lockResult = this.lock(key);
			if (lockResult.failed()) {
				throw new LockedException(lockResult.getMessage());
			}
		}
	}

	/**
	 * 加锁方法，通过返回值来判断加锁是否成功
	 */
	public Result<String> lock(Object key) throws Exception {
		return this.lock(key, userName);
	}

	private Result<String> lock(Object key, String value) throws Exception {
		CommonUtil.requireNonNull(key);
		String newKey = this.getKey(key.toString());
		RLock lock = redissonClient.getLock(newKey);
		boolean result = lock.tryLock(1, 5 * 60, TimeUnit.SECONDS);
		if (result) {
			RedisUtil.putWithSeconds(newKey + "_username", value, 5 * 60);
			this.lockedKeys.add(lock);
			Log.logger.info("加锁:{}", newKey);
			return Result.success();
		} else {
			String userName = RedisUtil.get(newKey + "_username");
			if (CommonUtil.isBlank(userName)) {
				return Result.fail("lock_error2");
			}
			return Result.fail("lock_error", new String[]{userName}, userName);
		}
	}

	private String getKey(String key) {
		return "LOCK_" + tenantId + "_" + key;
	}


	@Override
	public void close() {
		if (CommonUtil.isEmpty(this.lockedKeys)) {
			return;
		}
		this.lockedKeys.forEach(key -> {
			key.unlock();
			RedisUtil.delete(key.getName() + "_username");
			Log.logger.info("解锁:{}", key.getName());
		});
	}

}
