package com.wenge.redis;


import com.alibaba.fastjson2.JSON;
import com.wenge.common.util.CommonUtil;
import com.wenge.common.util.SpringUtil;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author HAO
 */
@SuppressWarnings({"unchecked", "unused"})
public class RedisUtil {

	private final static StringRedisTemplate stringRedisTemplate;

	private final static ValueOperations<String, String> stringValueOperations;

	private final static ListOperations<String, Object> listOperations;

	static {
		stringRedisTemplate = SpringUtil.getBean("stringRedisTemplate", StringRedisTemplate.class);
		stringValueOperations = SpringUtil.getBean("stringValueOperations", ValueOperations.class);
		listOperations = SpringUtil.getBean("listOperations", ListOperations.class);
	}

	/**
	 * Long leftPush(K key, V value); //left 是左面进入 也就是靠前 先进后出
	 */
	public static void leftPush(String key, Object value) {
		listOperations.leftPush(key, value);
	}

	public static void rightPush(String key, Object value) {
		listOperations.rightPush(key, value);
	}

	public static void leftPushAll(String key, Object... values) {
		listOperations.leftPushAll(key, values);
	}

	public static void rightPushAll(String key, Object... values) {
		listOperations.rightPushAll(key, values);
	}

	/**
	 * 从redis中获取值
	 */
	public static String get(String key) {
		return stringValueOperations.get(key);
	}

	public static <T> T get(String key, Class<T> clazz) {
		String result = get(key);
		if (CommonUtil.isBlank(result)) {
			return null;
		}
		return JSON.parseObject(result, clazz);
	}

	/**
	 * 向redis中存入值（无过期时间）
	 */
	public static void put(String key, String value) {
		stringValueOperations.set(key, value);
	}

	public static void put(String key, Object value) {
		put(key, JSON.toJSONString(value));
	}

	public static Boolean putIfAbsent(String key, String value) {
		return stringValueOperations.setIfAbsent(key, value);
	}

	/**
	 * 向redis中存入值（过期时间为秒）
	 */
	public static void putWithSeconds(String key, String value, long seconds) {
		putWithTime(key, value, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 向redis中存入值（过期时间为小时）
	 */
	public static void putWithHours(String key, String value, long hours) {
		putWithTime(key, value, hours, TimeUnit.HOURS);
	}

	/**
	 * 向redis中存入值（过期时间为分钟）
	 */
	public static void putWithMinutes(String key, String value, long minutes) {
		putWithTime(key, value, minutes, TimeUnit.MINUTES);
	}

	public static void putWithMinutes(String key, Object value, long minutes) {
		putWithMinutes(key, JSON.toJSONString(value), minutes);
	}

	/**
	 * 向redis中存入值（过期时间可自定义单位）
	 */
	private static void putWithTime(String key, String value, long time, TimeUnit timeUnit) {
		stringValueOperations.set(key, value, time, timeUnit);
	}

	/**
	 * 设置超时时间
	 */
	private static void expireWithTime(String key, long time, TimeUnit timeUnit) {
		stringRedisTemplate.expire(key, time, timeUnit);
	}

	/**
	 * 设置超时时间（秒）
	 */
	public static void expireWithSeconds(String key, long seconds) {
		expireWithTime(key, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 设置超时时间（小时）
	 */
	public static void expireWithHours(String key, long hours) {
		expireWithTime(key, hours, TimeUnit.HOURS);
	}

	/**
	 * 设置超时时间（分钟）
	 */
	public static void expireWithMinutes(String key, long minutes) {
		expireWithTime(key, minutes, TimeUnit.MINUTES);
	}

	public static void pushList(String key, List<?> list) {
		if (Objects.nonNull(list)
				&& !list.isEmpty()) {
			rightPushAll(key, list.toArray());
			stringRedisTemplate.expire(key, 1L, TimeUnit.DAYS);
		}
	}

	/**
	 * 命令用于移除并返回列表的第一个元素
	 */
	public static void leftPop(String key) {
		listOperations.leftPop(key);
	}

	public static void rightPop(String key) {
		listOperations.rightPop(key);
	}

	/**
	 * 删除列表中第一个遇到的value值。count指定删除多少个
	 * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
	 * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
	 * count = 0 : 移除表中所有与 VALUE 相等的值。
	 *
	 * @param key    key
	 * @param count  移除多少个相同的value
	 * @param object 要以除的value
	 */
	public static void removeList(String key, long count, Object object) {
		listOperations.remove(key, count, object);
	}

	/**
	 * 从redis中移除
	 */
	public static void delete(String key) {
		stringRedisTemplate.delete(key);
	}

	public static void delete(Collection<String> keys) {
		stringRedisTemplate.delete(keys);
	}

	/**
	 * 保留key指定范围内的列表值。其它的都删除。
	 *
	 * @param key   key
	 * @param start 起始位置
	 * @param end   结束位置，-1代表最后
	 */
	public static void trim(String key, long start, long end) {
		listOperations.trim(key, start, end);
	}

	/**
	 * 获取指定key的范围内的value值的 list列表
	 *
	 * @param key      key
	 * @param start    页码
	 * @param pageSize 一页多少条数据
	 */
	public static List<Object> getList(String key, int start, int pageSize) {
		long startIndex = (long) (start - 1) * pageSize;
		long endIndex = startIndex + pageSize - 1;
		return listOperations.range(key, startIndex, endIndex);
	}

	/**
	 * 获取所有数据
	 */
	public static List<Object> getList(String key) {
		return listOperations.range(key, 0L, -1L);
	}

	public static void putWithMap(String dataKey, String mapKey, String value) {
		stringRedisTemplate.opsForHash().put(dataKey, mapKey, value);
	}

	public static void putWithMapAll(String dataKey, Map<?, ?> map) {
		stringRedisTemplate.opsForHash().putAll(dataKey, map);
	}

	public static String getWithMap(String dataKey, String mapKey) {
		return (String) stringRedisTemplate.opsForHash().get(dataKey, mapKey);
	}

	/**
	 * 获取所有键值对
	 *
	 * @param dataKey
	 * @return
	 */
	public static Map<Object, Object> getWithMapAll(String dataKey) {
		return stringRedisTemplate.opsForHash().entries(dataKey);
	}

	public static List<String> multiGet(Collection<String> keys) {
		return stringRedisTemplate.opsForValue().multiGet(keys);
	}

	public static Set<String> keys(String pattern) {
		return stringRedisTemplate.keys(pattern);
	}

	public static void deleteWithMap(String dataKey, Collection<?> hashKeys) {
		if (CommonUtil.isEmpty(hashKeys)) {
			return;
		}
		stringRedisTemplate.opsForHash().delete(dataKey, hashKeys.stream().map(Object::toString).toArray());
	}

	public static Long getExpire(String key) {
		return stringRedisTemplate.getExpire(key);
	}

	public static Set<String> scan(String key) {
		try (Cursor<String> cursor = stringRedisTemplate.scan(ScanOptions.scanOptions().match(key).build());) {
			return cursor.stream().collect(Collectors.toSet());
		}
	}

	public static Set<String> scan(String key,long count) {
		try (Cursor<String> cursor = stringRedisTemplate.scan(ScanOptions.scanOptions().match(key).count(count).build());) {
			return cursor.stream().collect(Collectors.toSet());
		}
	}
}

