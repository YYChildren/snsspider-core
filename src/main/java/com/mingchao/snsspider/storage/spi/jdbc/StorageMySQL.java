package com.mingchao.snsspider.storage.spi.jdbc;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Session;

public class StorageMySQL extends StorageJdbc {
	public void insertIgnore(Object object) {
		final String sql = getInsertIgnoreSql(object);
		if(sql == null){
			return;
		}
		HibernateSql hs = new HibernateSql(){
			@Override
			public Object execute(Session session) throws Exception {
				session.createSQLQuery(sql).executeUpdate();
				return null;
			}
		};
		try {
			HibernateUtil.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}

	public void insertIgnore(final List<?> list) {
		HibernateSql hs = new HibernateSql(){
			@Override
			public Object execute(Session session) throws Exception {
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					String sql = getInsertIgnoreSql(object);
					if(sql == null){
						continue;
					}
					session.createSQLQuery(sql).executeUpdate();
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

	public void insertDuplicate(Object object) {
		final String sql = getInsertDuplicateSql(object);
		if(sql == null){
			return;
		}
		HibernateSql hs = new HibernateSql(){
			@Override
			public Object execute(Session session) throws Exception {
				session.createSQLQuery(sql).executeUpdate();
				return null;
			}
		};
		try {
			HibernateUtil.execute(hs);
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
					String sql = getInsertDuplicateSql(object);
					if(sql == null){
						continue;
					}
					session.createSQLQuery(sql).executeUpdate();
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
	
	public String getInsertIgnoreSql(Object object){
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
	
	public String getInsertDuplicateSql(Object object){
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
