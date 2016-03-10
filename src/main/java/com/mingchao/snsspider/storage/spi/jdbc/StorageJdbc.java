package com.mingchao.snsspider.storage.spi.jdbc;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mingchao.snsspider.storage.spi.BaseStorageSpi;

public class StorageJdbc extends BaseStorageSpi {
	
	protected final String FIELD_SE = "`";
	protected final String STRING_SE = "'";
	protected final String LEFT_BRACKETS = "(";
	protected final String RIGHT_BRACKETS = ")";

	public void insert(Object object) {
		try {
			HibernateUtil.save(object);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}

	public void insert(final List<?> list) {
		HibernateSql hs = new HibernateSql(){
			@Override
			public Object execute(Session session) throws Exception {
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					session.save(object);
				}
				return null;
			}
		};
		try {
			HibernateUtil.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}

	public void insertIgnore(Object object) {
		insert(object);
	}

	public void insertIgnore(List<?> list) {
		insert(list);
	}

	public void insertDuplicate(Object object) {
		try {
			HibernateUtil.saveOrUpdate(object);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}

	public void insertDuplicate(final List<?> list) {
		HibernateSql hs = new HibernateSql(){
			@Override
			public Object execute(Session session) throws Exception {
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					session.saveOrUpdate(object);
				}
				return null;
			}
		};
		try {
			HibernateUtil.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}

	@Override
	public Object get(Class<?> c, Serializable id) {
		try {
			return HibernateUtil.get(c, id);
		} catch (Exception e) {
			log.warn(e, e);
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Object> get(final Class<?> c, final Serializable idStart, final Serializable idEnd) {
		HibernateSql hs = new HibernateSql(){
			@Override
			public Object execute(Session session) throws Exception {
				 String fieldName = getIdFieldName(c);
				 log.debug(fieldName);
				 return session.createCriteria(c)
						 .add(Restrictions.ge(fieldName, idStart))
						 .add(Restrictions.lt(fieldName, idEnd))
						 .addOrder(Order.asc(fieldName))
						 .list();
			}
		};
		try {
			return (List)HibernateUtil.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
			return null;
		}
		
	}
	
	//TODO
	@Override
	public void delete(final Class<?> c, final Serializable idStart, final Serializable idEnd) {
		HibernateSql hs = new HibernateSql(){
			@Override
			public Object execute(Session session) throws Exception {
				String deleteSql = deleteSql(c,idStart, idEnd);
				 return session.createSQLQuery(deleteSql).executeUpdate();
			}
		};
		try {
			HibernateUtil.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}
	
	@Override
	public void close() {
		HibernateUtil.close();
	}

	@Override
	public Boolean hasMore(final Class<?> c, final Serializable idStart) {
		HibernateSql hs = new HibernateSql() {
			@Override
			public Object execute(Session session) throws Exception {
				String queryString = hasMoreSql(c, idStart);
				BigInteger o = (BigInteger)session.createSQLQuery(queryString).uniqueResult();
				log.debug("has more: " + o.getClass().getName() + "values: "+ o);
				return o.intValue() == 1;
			}
		};
		try {
			return (Boolean) HibernateUtil.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
			return null;
		}
	}
	
	public String hasMoreSql(Class<?> c, Serializable idStart){
		String table = getTableName(c);
		String fieldName = getIdFieldName(c);
		String queryString = "SELECT COUNT(*) > 0 FROM "+ table 
				 + " WHERE "+ fieldName + ">=" + idStart;
		return queryString;
	}
	
	public String deleteSql(Class<?> c, Serializable idStart, Serializable idEnd){
		String table = getTableName(c);
		String fieldName = getIdFieldName(c);
		String queryString = "DELETE FROM "+ table 
				 + " WHERE "+ fieldName + ">=" + idStart 
				 + " AND " + fieldName + "<" + idEnd ;
		return queryString;
	}
	
	protected String getIdFieldName(Class<?> c){
		Method[] methods = c.getMethods();
		Method idMethod = null;
		for (int i = 0; i < methods.length; i++) {
			if(methods[i].getAnnotation(Id.class) != null){
				idMethod = methods[i];
			}
		}
		String field = null;
		if(idMethod != null){
			String methodName = idMethod.getName();
			if(methodName.startsWith("get")){
				field = methodName.substring(3).toLowerCase();
			}else if(methodName.startsWith("is")){
				field = methodName.substring(2).toLowerCase();
			}
		}
		return field == null ? null : field;
	}
	
	protected String getTableName(Object object){
		
		if(object == null){
			return null;
		}
		Class<?> clazz = object.getClass();
		return getTableName(clazz);
	}
	
	protected String getTableName(Class<?> clazz){
		if(clazz == null){
			return null;
		}
		String table = null;
		try {
			 table = clazz.getAnnotation(Entity.class).name();
			 table = table.equals("") ? clazz.getSimpleName().toLowerCase(): table;
		} catch (NullPointerException e) {
			table = clazz.getSimpleName().toLowerCase();
		}
		return FIELD_SE + table + FIELD_SE;
	}
	
	protected Entry<String,String> getKVString(List<Entry<String, Object>> kvs){
		StringBuilder keyBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		
		for (Entry<String, Object> entry : kvs) {
			keyBuilder.append(entry.getKey()).append(", ");
			Object value = entry.getValue();
			if(value instanceof String){
				valueBuilder.append(STRING_SE).append(value).append(STRING_SE).append(", ");
			}else{
				valueBuilder.append(value).append(", ");
			}
		}
		
		String keys = LEFT_BRACKETS + keyBuilder.substring(0, keyBuilder.length() - 2) + RIGHT_BRACKETS;
		String values = LEFT_BRACKETS + valueBuilder.substring(0, valueBuilder.length() - 2) + RIGHT_BRACKETS;
		
		return new SimpleEntry<String,String>(keys,values);
	}
	
	protected List<Entry<String, Object>> getKVs(Object object){
		Class<?> clazz = object.getClass();
		Field [] fileds = clazz.getDeclaredFields();
		List<Entry<String, Object>> kvs = new ArrayList<Entry<String, Object>>();
		for (int i = 0; i < fileds.length; i++) {
			fileds[i].setAccessible(true);
			try {
				Object v = fileds[i].get(object);
				if(v != null){
					kvs.add(new SimpleEntry<String, Object>(FIELD_SE + fileds[i].getName() +FIELD_SE, v));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return kvs;
	}
	
}
