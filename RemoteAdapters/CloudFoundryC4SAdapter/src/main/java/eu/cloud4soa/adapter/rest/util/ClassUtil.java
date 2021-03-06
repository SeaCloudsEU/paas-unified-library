/*
 * Copyright [2013] [Cloud4SOA, www.cloud4soa.eu]
 *
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package eu.cloud4soa.adapter.rest.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import eu.cloud4soa.adapter.rest.aop.Ignore;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class ClassUtil {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getClassAnnotationValue(Class source, Class annotation, String attributeName, Class<T> expected) {
		Annotation instance = source.getAnnotation(annotation);
		T value = null;
		if (instance != null) {
			try {
				value = (T) instance.annotationType().getMethod(attributeName).invoke(instance);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	public static Field[] getAllDeclaredFields(Class<?> clazz, Comparable...exclude){
		return getAllDeclaredFields(clazz, Arrays.asList(exclude));
	}
	
	@SuppressWarnings({"rawtypes" })
	public static Field[] getAllDeclaredFields(Class<?> clazz, List<Comparable> exclude){
		Field[] declaredFields = clazz.getDeclaredFields();
		Class<?> superClass = clazz.getSuperclass();
		if(superClass!=null && superClass!=Object.class){
			declaredFields = ArrayUtils.concat(declaredFields, getAllDeclaredFields(superClass, exclude));
		}
		
		List<Field> asList = new LinkedList<Field>();
		for(Field field : declaredFields){
//			for(Object toExclude : exclude){
//				if(null != field.getAnnotation(Ignore.class)){
//					continue;
//				}else if(toExclude instanceof String && toExclude.equals(field.getName())){
//					continue;
//				}
//			}
			if( (null!=field.getAnnotation(Ignore.class)) || exclude.contains(field.getName())){
				continue;
			}
			asList.add(field);
		}
		return asList.toArray(new Field[asList.size()]);
	}
	
	public static <T> T getValueOf(String fieldName, Object reference, Class<?> referenceClazz, Class<T> valueType){
		try{
			Field field = referenceClazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			
			@SuppressWarnings("unchecked")
			T toReturn = (T) field.get(reference);
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
