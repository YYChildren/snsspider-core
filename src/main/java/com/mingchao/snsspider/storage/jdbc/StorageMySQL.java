package com.mingchao.snsspider.storage.jdbc;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.mingchao.snsspider.storage.util.HibernateSql;
import com.mingchao.snsspider.storage.util.HibernateUtil;
import com.mingchao.snsspider.storage.util.SQLUtil;

public class StorageMySQL extends StorageJdbc {
	
	public StorageMySQL(HibernateUtil hu){
		super(hu);
	}
	
	public void insertIgnore(Object object) {
		final String sql = SQLUtil.getInsertIgnoreSql(object);
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
			hu.execute(hs);
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
					String sql = SQLUtil.getInsertIgnoreSql(object);
					if(sql == null){
						continue;
					}
					session.createSQLQuery(sql).executeUpdate();
				}
				return null;
			}
		};
		try {
			hu.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}

	public void insertDuplicate(Object object) {
		final String sql = SQLUtil.getInsertDuplicateSql(object);
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
			hu.execute(hs);
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
					String sql = SQLUtil.getInsertDuplicateSql(object);
					if(sql == null){
						continue;
					}
					session.createSQLQuery(sql).executeUpdate();
				}
				return null;
			}
		};
		try {
			hu.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}
}
