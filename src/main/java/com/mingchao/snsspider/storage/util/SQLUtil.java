package com.mingchao.snsspider.storage.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.Id;

public class SQLUtil {
	public static final String STRING_SE = "'";
	public static final String LEFT_BRACKETS = "(";
	public static final String RIGHT_BRACKETS = ")";

	public static String getTableName(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> clazz = object.getClass();
		return getTableName(clazz);
	}

	public static String getTableName(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		String table = null;
		try {
			table = clazz.getAnnotation(Entity.class).name();
			table = table.equals("") ? clazz.getSimpleName().toLowerCase()
					: table;
		} catch (NullPointerException e) {
			table = clazz.getSimpleName().toLowerCase();
		}
		return table;
	}

	public static String getIdFieldName(Class<?> c) {
		Method[] methods = c.getMethods();
		Method idMethod = null;
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getAnnotation(Id.class) != null) {
				idMethod = methods[i];
			}
		}
		String field = null;
		if (idMethod != null) {
			String methodName = idMethod.getName();
			if (methodName.startsWith("get")) {
				field = methodName.substring(3).toLowerCase();
			} else if (methodName.startsWith("is")) {
				field = methodName.substring(2).toLowerCase();
			}
		}
		return field == null ? null : field;
	}

	public static String hasMoreSql(Class<?> c, Serializable idStart) {
		String table = SQLUtil.getTableName(c);
		String fieldName = SQLUtil.getIdFieldName(c);
		String queryString = "SELECT COUNT(*) > 0 FROM " + table + " WHERE "
				+ fieldName + ">=" + idStart;
		return queryString;
	}

	public static String deleteSql(Class<?> c, Serializable idStart,
			Serializable idEnd) {
		String table = SQLUtil.getTableName(c);
		String fieldName = SQLUtil.getIdFieldName(c);
		String queryString = "DELETE FROM " + table + " WHERE " + fieldName
				+ ">=" + idStart + " AND " + fieldName + "<" + idEnd;
		return queryString;
	}

	public static Entry<String, String> getKVString(
			List<Entry<String, Object>> kvs) {
		StringBuilder keyBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();

		for (Entry<String, Object> entry : kvs) {
			keyBuilder.append(entry.getKey()).append(", ");
			Object value = entry.getValue();
			if (value instanceof String) {
				valueBuilder.append(STRING_SE).append(value).append(STRING_SE)
						.append(", ");
			} else {
				valueBuilder.append(value).append(", ");
			}
		}
		String keys = LEFT_BRACKETS
				+ keyBuilder.substring(0, keyBuilder.length() - 2)
				+ RIGHT_BRACKETS;
		String values = LEFT_BRACKETS
				+ valueBuilder.substring(0, valueBuilder.length() - 2)
				+ RIGHT_BRACKETS;
		return new SimpleEntry<String, String>(keys, values);
	}

	public static List<Entry<String, Object>> getKVs(Object object) {
		Class<?> clazz = object.getClass();
		Field[] fileds = clazz.getDeclaredFields();
		List<Entry<String, Object>> kvs = new ArrayList<Entry<String, Object>>();
		for (int i = 0; i < fileds.length; i++) {
			fileds[i].setAccessible(true);
			try {
				Object v = fileds[i].get(object);
				if (v != null) {
					kvs.add(new SimpleEntry<String, Object>(
							fileds[i].getName(), v));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return kvs;
	}

	public static String getInsertIgnoreSql(Object object){
		List<Entry<String, Object>> kvs = getKVs(object);
		if(kvs.isEmpty()){
			return null;
		}
		Entry<String,String> kvsString = getKVString(kvs);
		return new StringBuilder()
			.append("INSERT IGNORE INTO ")
			.append(getTableName(object))
			.append(kvsString.getKey())
			.append(" VALUES")
			.append(kvsString.getValue())
			.toString();
	}
	
	public static String getInsertDuplicateSql(Object object){
		List<Entry<String, Object>> kvs = getKVs(object);
		if(kvs.isEmpty()){
			return null;
		}
		Entry<String,String> kvsString = getKVString(kvs);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("INSERT INTO ")
			.append(getTableName(object))
			.append(kvsString.getKey())
			.append(" VALUES")
			.append(kvsString.getValue())
			.append(" ON DUPLICATE KEY UPDATE ");
		for (Entry<String, Object> entry : kvs) {
			String key = entry.getKey();
			sqlBuilder.append(key).append("=VALUES")
				.append(LEFT_BRACKETS).append(key).append(RIGHT_BRACKETS)
				.append(", ");
		}
		return sqlBuilder.substring(0, sqlBuilder.length() - 2);
	}

}
