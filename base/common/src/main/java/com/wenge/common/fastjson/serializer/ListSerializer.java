///*
// * Copyright 1999-2018 Alibaba Group.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.wenge.common.fastjson.serializer;
//
//
//import com.alibaba.fastjson.serializer.*;
//import com.alibaba.fastjson.util.TypeUtils;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.List;
//
//public class ListSerializer implements ObjectSerializer {
//
//	public static final ListSerializer instance = new ListSerializer();
//
//	@Override
//	public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
//			throws IOException {
//
//		boolean writeClassName = serializer.out.isEnabled(SerializerFeature.WriteClassName)
//				|| SerializerFeature.isEnabled(features, SerializerFeature.WriteClassName);
//
//		SerializeWriter out = serializer.out;
//
//		Type elementType = null;
//		if (writeClassName) {
//			elementType = TypeUtils.getCollectionItemType(fieldType);
//		}
//
//		if (object == null) {
//			out.writeNull(SerializerFeature.WriteNullListAsEmpty);
//			return;
//		}
//
//		List<?> list = (List<?>) object;
//
//		if (list.size() == 0) {
//			out.append("[]");
//			return;
//		}
//
//		ObjectSerializer itemSerializer;
//		out.append('[');
//		for (int i = 0, size = list.size(); i < size; ++i) {
//			Object item = list.get(i);
//			if (i != 0) {
//				out.append(',');
//			}
//
//			if (item == null) {
//				out.append("null");
//			} else {
//				Class<?> clazz = item.getClass();
//
//				if (clazz == Integer.class) {
//					out.writeInt((int) item);
//				} else if (clazz == Long.class) {
//					long val = (long) item;
//					if (writeClassName) {
//						out.writeLong(val);
//						out.write('L');
//					} else {
//						out.writeString(val + "");
//					}
//				} else {
//					if ((SerializerFeature.DisableCircularReferenceDetect.mask & features) != 0) {
//						itemSerializer = serializer.getObjectWriter(item.getClass());
//						itemSerializer.write(serializer, item, i, elementType, features);
//					} else {
//						if (serializer.containsReference(item)) {
//							serializer.writeReference(item);
//						} else {
//							itemSerializer = serializer.getObjectWriter(item.getClass());
//							if ((SerializerFeature.WriteClassName.mask & features) != 0
//									&& itemSerializer instanceof JavaBeanSerializer) {
//								JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) itemSerializer;
//								javaBeanSerializer.writeNoneASM(serializer, item, i, elementType, features);
//							} else {
//								itemSerializer.write(serializer, item, i, elementType, features);
//							}
//						}
//					}
//				}
//			}
//		}
//		out.append(']');
//	}
//
//}
