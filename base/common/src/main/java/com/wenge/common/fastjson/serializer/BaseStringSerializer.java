package com.wenge.common.fastjson.serializer;


import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;

/**
 * @author HAO灏 2021/2/8 12:43
 */
public abstract class BaseStringSerializer<T> implements ObjectWriter<T> {

	@SuppressWarnings("unchecked")
	@Override
	public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
		if (object == null) {
			jsonWriter.writeNull();
			return;
		}
		jsonWriter.writeString(this.apply((T) object));
	}

	public abstract String apply(T t);

}
