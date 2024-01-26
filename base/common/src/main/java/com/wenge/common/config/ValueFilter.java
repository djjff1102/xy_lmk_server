package com.wenge.common.config;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.ReflectUtil;
import com.wenge.common.constants.AuthConstants;
import com.wenge.common.constants.Constants;
import com.wenge.common.data_permission.DataPermission;
import com.wenge.common.data_permission.DataPermissions;
import com.wenge.common.fastjson.serializer.BigIntegerSerializer;
import com.wenge.common.fastjson.serializer.DateSerializer;
import com.wenge.common.fastjson.serializer.LongSerializer;
import com.wenge.common.util.CommonUtil;
import com.wenge.dto.user.UserDTO;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author HAO灏 2022/12/22 22:00
 */
public class ValueFilter implements com.alibaba.fastjson2.filter.ValueFilter {

	private final static Map<Class<?>, Function<Object, Object>> valueFilters = CommonUtil.ofMapN(
			Long.class, (Function<Object, Object>) value -> LongSerializer.instance.apply((Long) value),
			Long.class, (Function<Object, Object>) value -> LongSerializer.instance.apply((Long) value),
			BigInteger.class, (Function<Object, Object>) value -> BigIntegerSerializer.instance.apply((BigInteger) value),
			Date.class, (Function<Object, Object>) value -> DateSerializer.instance.apply((Date) value));

	//Map<类名#字段名, Map<pCode,code>>
	private static final Map<String, Map<String, String>> dataPermissionsMap = new ConcurrentHashMap<>();
	private static final Set<String> noDataPermissionsSet = new ConcurrentHashSet<>();

	public final static ValueFilter INSTANCE = new ValueFilter();

	@Override
	public Object apply(Object object, String name, Object value) {
		if (value == null) {
			return null;
		}
		UserDTO userDTO;
		String pCode;
		if ((userDTO = AuthConstants.getCurrentUser()) == null || CommonUtil.isBlank(pCode = Constants.REQUEST_DATA_PERMISSION_CODE.get())) {
			return this.getValue(value);
		}
		String key = object.getClass().getName() + "#" + name;
		if (noDataPermissionsSet.contains(key)) {
			return this.getValue(value);
		}
		Map<String, String> dataPermissions = dataPermissionsMap.get(key);
		if (dataPermissions == null) {
			Field field = ReflectUtil.getField(object.getClass(), name);
			if (field == null) {
				noDataPermissionsSet.add(key);
				return this.getValue(value);
			} else {
				DataPermissions dataPermissionsAnno = field.getAnnotation(DataPermissions.class);
				if (dataPermissionsAnno == null || CommonUtil.isEmpty(dataPermissionsAnno.value())) {
					noDataPermissionsSet.add(key);
					return this.getValue(value);
				} else {
					dataPermissions = new ConcurrentHashMap<>();
					for (DataPermission dataPermission : dataPermissionsAnno.value()) {
						dataPermissions.put(dataPermission.pCode(), dataPermission.code());
					}
					dataPermissionsMap.put(key, dataPermissions);
				}
			}
		}
		Set<String> dataPermissionCode = userDTO.getDataPermissionCode();
		if (CommonUtil.isNotBlank(dataPermissions.get(pCode)) && dataPermissionCode.contains(dataPermissions.get(pCode))) {
			return this.getValue(value);
		}
		return Constants.NO_DATA_PERMISSION_STR;
	}

	private Object getValue(Object value) {
		return Optional.ofNullable(valueFilters.get(value.getClass())).orElse(o -> o).apply(value);
	}

}
