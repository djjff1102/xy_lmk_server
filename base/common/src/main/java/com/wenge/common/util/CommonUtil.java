package com.wenge.common.util;

import com.alibaba.fastjson2.JSON;
import com.wenge.common.constants.AuthConstants;
import com.wenge.common.constants.Constants;
import com.wenge.dto.user.UserDTO;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author HAO 2019/03/19 20:42
 * 不要随便扩展此类，此类中不要涉及业务，即脱离当前业务系统后，也可以使用
 */
public class CommonUtil {

	private final static ThreadLocal<SimpleDateFormat> simpleDateFormat = ThreadLocal.withInitial(SimpleDateFormat::new);
	private final static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z"};

	private CommonUtil() {
	}

	public static <T> List<T> emptyList() {
		return new ArrayList<>();
	}

	public static <T> Set<T> emptySet() {
		return new HashSet<>();
	}

	public static <K, V> Map<K, V> emptyMap() {
		return new HashMap<>();
	}

	/**
	 * 对象转为String
	 */
	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 判断集合类是否为空
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断集合类是否为非空
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static <T> void isNotEmptyThen(Collection<T> collection, Consumer<Collection<T>> consumer) {
		if (isNotEmpty(collection)) {
			consumer.accept(collection);
		}
	}

	public static <T> void isNotEmptyThenFor(Collection<T> collection, Consumer<T> consumer) {
		if (isNotEmpty(collection)) {
			collection.forEach(consumer);
		}
	}

	public static <T> void isEmptyThen(Collection<T> collection, Consumer<Collection<T>> consumer) {
		if (isEmpty(collection)) {
			consumer.accept(collection);
		}
	}

	/**
	 * 判断Map是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 判断Map是否为非空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static <K, V> void isNotEmptyThen(Map<K, V> map, Consumer<Map<K, V>> consumer) {
		if (isNotEmpty(map)) {
			consumer.accept(map);
		}
	}

	/**
	 * 判断数组是否为空
	 */
	public static <T> boolean isEmpty(T[] arr) {
		return arr == null || arr.length == 0;
	}

	public static <T> boolean isNotEmpty(T[] arr) {
		return !isEmpty(arr);
	}

	public static boolean isEmpty(long[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isNotEmpty(long[] arr) {
		return !isEmpty(arr);
	}

	public static boolean isEmpty(char[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isEmpty(byte[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isNotEmpty(byte[] arr) {
		return !isEmpty(arr);
	}

	public static boolean isNotEmpty(char[] arr) {
		return !isEmpty(arr);
	}

	public static boolean isEmpty(int[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isNotEmpty(int[] arr) {
		return !isEmpty(arr);
	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs != null && (strLen = cs.length()) != 0) {
			for (int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	public static void isBlankThen(CharSequence cs, Consumer<CharSequence> consumer) {
		if (isBlank(cs)) {
			consumer.accept(cs);
		}
	}

	public static void isNotBlankThen(CharSequence cs, Consumer<CharSequence> consumer) {
		if (isNotBlank(cs)) {
			consumer.accept(cs);
		}
	}

	/**
	 * 判断字符串是否为非空
	 */
	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * 判断StringJoiner是否为空
	 */
	public static boolean isBlank(StringJoiner stringJoiner) {
		return stringJoiner == null || stringJoiner.length() == 0;
	}

	/**
	 * 判断StringJoiner是否为非空
	 */
	public static boolean isNotBlank(StringJoiner stringJoiner) {
		return !isBlank(stringJoiner);
	}

	/**
	 * 转为int
	 */
	public static int toInt(Object obj) {
		return toInt(obj, -1);
	}

	public static double toDouble(Object obj) {
		return toDouble(obj, -1d);
	}

	public static double toDouble(Object obj, double defaultValue) {
		String str;
		return isBlank(str = toString(obj)) ? defaultValue : Double.parseDouble(str);
	}

	public static int toInt(Object obj, int defaultValue) {
		String str;
		if (isBlank(str = toString(obj))) {
			return defaultValue;
		} else if (str.equalsIgnoreCase("true")) {
			return 1;
		} else if (str.equalsIgnoreCase("false")) {
			return 0;
		} else {
			return Integer.parseInt(str);
		}
	}

	public static long toLong(Object obj) {
		return toLong(obj, -1L);
	}

	public static long toLong(Object obj, long defaultValue) {
		String str;
		return isBlank(str = toString(obj)) ? defaultValue : Long.parseLong(str);
	}

	/**
	 * 转为BigDecimal
	 */
	public static BigDecimal toBigDecimal(Object obj) {
		return toBigDecimal(obj, new BigDecimal("-1"));
	}

	public static BigDecimal toBigDecimal(Object obj, BigDecimal defaultValue) {
		String str;
		return isBlank(str = toString(obj)) ? defaultValue : new BigDecimal(str);
	}

	public static BigInteger toBigInteger(Object obj) {
		return toBigInteger(obj, new BigInteger("-1"));
	}

	///**
	// * 数据过滤，保留想要的字段
	// *
	// * @param data       数据源
	// * @param columns    要保留的字段，可以是多个，逗号分割即可
	// * @param resultType 返回的数据类型
	// * @return 结果List
	// */
	//public static <T> List<T> filterData(Collection<?> data, String columns, Class<T> resultType) {
	//	PropertyFilter propertyFilter = (o, s, o1) -> columns.toLowerCase().contains(s.toLowerCase());
	//	return data.parallelStream().map(
	//					obj -> JSON.parseObject(JSON.toJSONString(obj, SerializeConfig.globalInstance, new SerializeFilter[]{propertyFilter}, "yyyy-MM-dd", JSON.DEFAULT_GENERATE_FEATURE), resultType))
	//			.collect(Collectors.toList());
	//}

	public static BigInteger toBigInteger(Object obj, BigInteger defaultValue) {
		String str;
		return isBlank(str = toString(obj)) ? defaultValue : new BigInteger(str);
	}

	/**
	 * 转为Boolean
	 */
	public static boolean toBoolean(Object obj) {
		if (obj instanceof Integer) {
			return toInt(obj) == 1;
		}
		return Boolean.parseBoolean(toString(obj));
	}

	/**
	 * 对数据进行分页
	 *
	 * @param data      数据源
	 * @param startPage 页码
	 * @param pageSize  每页数据量
	 * @return 分页后的数据
	 */
	public static <T> List<T> limit(Collection<T> data, int startPage, int pageSize) {
		if (isNotEmpty(data) && startPage > 0 && pageSize > 0) {
			return data.parallelStream()
					.skip((long) (startPage - 1) * pageSize)
					.limit(pageSize)
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	/**
	 * 生成List
	 */
	@SafeVarargs
	public static <T> List<T> ofList(T... data) {
		return ofList(new ArrayList<>(), data);
	}

	@SafeVarargs
	public static <T> List<T> ofLinkedList(T... data) {
		return ofList(new LinkedList<>(), data);
	}

	@SafeVarargs
	private static <T> List<T> ofList(List<T> list, T... data) {
		if (isEmpty(data)) {
			return list;
		}
		Collections.addAll(list, data);
		return list;
	}

	/**
	 * 生成Set
	 */
	@SafeVarargs
	public static <T> Set<T> ofSet(T... data) {
		if (isEmpty(data)) {
			return new HashSet<>();
		}
		Set<T> set;
		Collections.addAll(set = new HashSet<>(data.length), data);
		return set;
	}

	/**
	 * 生成map
	 */
	public static <K, V> Map<K, V> ofMap(K k, V v) {
		return ofMapN(k, v);
	}

	public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2) {
		return ofMapN(k1, v1, k2, v2);
	}

	public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3) {
		return ofMapN(k1, v1, k2, v2, k3, v3);
	}

	public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		return ofMapN(k1, v1, k2, v2, k3, v3, k4, v4);
	}

	public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		return ofMapN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
	}

	public static <K, V> Map<K, V> ofMapN(Object... input) {
		return ofMapN(new HashMap<>(), input);
	}

	public static <K, V> Map<K, V> ofLinkedHashMapN(Object... input) {
		return ofMapN(new LinkedHashMap<>(), input);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> ofMapN(Map<K, V> map, Object... input) {
		if (isEmpty(input)) {
			return map;
		}
		//input的长度必须为2的整数倍
		if ((input.length & 1) != 0) {
			throw new IllegalArgumentException("长度必须为2的整数倍");
		}
		for (int i = 0; i < input.length; i += 2) {
			K k = Objects.requireNonNull((K) input[i]);
			V v = (V) input[i + 1];
			map.put(k, v);
		}
		return map;
	}

	public static String formatSQLInCondition(Collection<?> data) {
		return formatSQLInCondition(data.stream());
	}

	public static String formatSQLInCondition(String... data) {
		return formatSQLInCondition(Stream.of(data));
	}

	public static String formatSQLInCondition(String data) {
		return formatSQLInCondition(data.split(","));
	}

	private static String formatSQLInCondition(Stream<?> stream) {
		String s = stream.map(CommonUtil::toString)
				.collect(Collectors.joining("','", "'", "'"));
		return isBlank(s) ? "''" : s;
	}

	/**
	 * 复制对象
	 * 添加这个方法的原因是，apache中也有和Spring一样的方法BeanUtils.copyProperties，
	 * 但是apache的有时候会有问题，通过CommonUtil中添加这个方法，统一为Spring的
	 * 这方法不需要自己new一个target
	 */
	public static <T> T copyProperties(Object source, Class<T> clazz) throws Exception {
		Objects.requireNonNull(source);
		Objects.requireNonNull(clazz);
		Object target = clazz.newInstance();
		BeanUtils.copyProperties(source, target);
		return clazz.cast(target);
	}

	public static void copyProperties(Object source, Object target, String... ignoreProperties) {
		BeanUtils.copyProperties(source, target, ignoreProperties);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] addToArray(Class<T> clazz, T[] array, T... data) {
		requireNonNull(array);
		requireNonNull(data);
		T[] newArr = (T[]) Array.newInstance(clazz, array.length + data.length);
		System.arraycopy(array, 0, newArr, 0, array.length);
		System.arraycopy(data, 0, newArr, newArr.length - data.length, data.length);
		return newArr;
	}

	public static void copyPropertiesIgnoreDefault(Object source, Object target, String... ignoreProperties) {
		String[] defaultIgnore = {"enterpriseId", "id", "createDate", "modifyDate"};
		if (isEmpty(ignoreProperties)) {
			copyProperties(source, target, defaultIgnore);
		} else {
			String[] s = addToArray(String.class, defaultIgnore, ignoreProperties);
			copyProperties(source, target, s);
		}
	}

	public static void requireNonNull(Object obj) {
		Objects.requireNonNull(obj);
		if (obj instanceof String && isBlank((String) obj)) {
			throw new NullPointerException();
		}
	}

	public static String formatDate(Date date, String pattern) {
		requireNonNull(date);
		requireNonNull(pattern);
		simpleDateFormat.get().applyPattern(pattern);
		return simpleDateFormat.get().format(date);
	}

	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseDate(String date) throws ParseException {
		return parseDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseDate(String date, String pattern) throws ParseException {
		requireNonNull(date);
		requireNonNull(pattern);
		simpleDateFormat.get().applyPattern(pattern);
		return simpleDateFormat.get().parse(toString(date));
	}

	/**
	 * 判断字符串是否为数字
	 */
	public static boolean isNumeric(String str) {
		return !isBlank(str) && str.matches("^([-+])?\\d+(\\.\\d+)?$");
	}

	//public static <T> T cast(Object source, Class<T> targetClass) {
	//	return TypeUtils.castToJavaBean(source, targetClass);
	//}

	public static boolean isNotNumeric(String str) {
		return !isNumeric(str);
	}

	public static <T> List<T> cast(Collection<?> collection, Class<T> targetClass) {
		if (CommonUtil.isEmpty(collection) || targetClass == null) {
			return null;
		}
		return JSON.parseArray(JSON.toJSONString(collection), targetClass);
	}

	public static boolean checkPermission(BigInteger needCheckValue, BigInteger value) {
		if (needCheckValue == null || needCheckValue.compareTo(BigInteger.ZERO) < 0) {
			return false;
		}
		return value.compareTo(BigInteger.ZERO) == 0 ? needCheckValue.compareTo(BigInteger.ZERO) == 0L : needCheckValue.and(value).compareTo(value) == 0;
	}

	public static boolean checkPermission(String needCheckValue, BigInteger value) {
		BigInteger needCheckValue0 = toBigInteger(needCheckValue);
		if (needCheckValue0 == null || needCheckValue0.compareTo(BigInteger.ZERO) < 0) {
			return false;
		}
		return value.compareTo(BigInteger.ZERO) == 0 ? needCheckValue0.compareTo(BigInteger.ZERO) == 0L : needCheckValue0.and(value).compareTo(value) == 0;
	}

	public static boolean checkPermission(int needCheckValue, int value) {
		return checkPermission(BigInteger.valueOf(needCheckValue), BigInteger.valueOf(value));
	}

	public static float daysBetween(long day1, long day2) {
		long time = day1 - day2;
		time = Math.abs(time);
		return time / (24F * 60 * 60 * 1000);
	}

	public static String randomNum(int length) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			result.append(random.nextInt(10));
		}
		return result.toString();
	}

	public static String now() {
		return formatDate(new Date());
	}

	/**
	 * @param size 每个list中的数据量
	 */
	public static <T> List<List<T>> split(List<T> target, int size) {
		if (isEmpty(target) || size <= 0) {
			return emptyList();
		}
		int remainder = target.size() % size;
		int resultSize = target.size() / size;
		List<List<T>> result = new ArrayList<>(resultSize);
		for (int i = 0; i < resultSize; i++) {
			result.add(target.subList(i * size, (i + 1) * size));
		}
		if (remainder > 0) {
			result.add(target.subList(resultSize * size, resultSize * size + remainder));
		}
		return result;
	}

	/**
	 * @param size 切分成多少个list
	 */
	public static <T> List<List<T>> split0(List<T> target, int size) {
		List<List<T>> result = new ArrayList<>();
		int remainder = target.size() % size;
		int number = target.size() / size;
		int offset = 0;
		for (int i = 0; i < size; i++) {
			List<T> value;
			if (remainder > 0) {
				value = target.subList(i * number + offset, (i + 1) * number + offset + 1);
				remainder--;
				offset++;
			} else {
				value = target.subList(i * number + offset, (i + 1) * number + offset);
			}
			if (CommonUtil.isEmpty(value)) {
				break;
			}
			result.add(value);
		}
		return result;
	}

	public static String shortUUID() {
		return shortUUID(8);
	}

	public static String shortUUID(int length) {
		StringBuilder s = new StringBuilder();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < length; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			s.append(chars[x % 0x3E]);
		}
		return s.toString();
	}

	public static boolean checkAuthority(Object... pCode_codes) {
		Map<String, String> pCode_code = ofMapN(pCode_codes);
		UserDTO userDTO;
		String requestPCode;
		if ((userDTO = AuthConstants.getCurrentUser()) == null || CommonUtil.isBlank(requestPCode = Constants.REQUEST_DATA_PERMISSION_CODE.get())) {
			return true;
		}
		if (CommonUtil.isEmpty(pCode_code) || CommonUtil.isBlank(pCode_code.get(requestPCode))) {
			return false;
		}
		Set<String> dataPermissionCode = userDTO.getDataPermissionCode();
		return dataPermissionCode.contains(pCode_code.get(requestPCode));
	}
}
