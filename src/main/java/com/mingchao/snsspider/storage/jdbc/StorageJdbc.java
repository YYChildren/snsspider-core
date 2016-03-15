package com.mingchao.snsspider.storage.jdbc;


import java.io.Serializable;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mingchao.snsspider.storage.BaseStorage;
import com.mingchao.snsspider.storage.util.HibernateSql;
import com.mingchao.snsspider.storage.util.HibernateUtil;
import com.mingchao.snsspider.storage.util.SQLUtil;

public class StorageJdbc extends BaseStorage {
	protected HibernateUtil hu;
	
	public StorageJdbc(HibernateUtil hu){
		this.hu = hu;
	}

	public void insert(Object object) {
		try {
			hu.save(object);
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
			hu.execute(hs);
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
			hu.saveOrUpdate(object);
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
			hu.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}

	@Override
	public Object get(Class<?> c, Serializable id) {
		try {
			return hu.get(c, id);
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
				 String fieldName = SQLUtil.getIdFieldName(c);
				 log.debug(fieldName);
				 return session.createCriteria(c)
						 .add(Restrictions.ge(fieldName, idStart))
						 .add(Restrictions.lt(fieldName, idEnd))
						 .addOrder(Order.asc(fieldName))
						 .list();
			}
		};
		try {
			return (List)hu.execute(hs);
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
				String deleteSql = SQLUtil.deleteSql(c,idStart, idEnd);
				 return session.createSQLQuery(deleteSql).executeUpdate();
			}
		};
		try {
			hu.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
		}
	}
	
	public Object execute(HibernateSql hs) throws Exception{
		return hu.execute(hs);
	}
	
	@Override
	public void close() {
		hu.close();
	}

	@Override
	public Boolean hasMore(final Class<?> c, final Serializable idStart) {
		HibernateSql hs = new HibernateSql() {
			@Override
			public Object execute(Session session) throws Exception {
				String queryString = SQLUtil.hasMoreSql(c, idStart);
				BigInteger o = (BigInteger)session.createSQLQuery(queryString).uniqueResult();
				log.debug("has more: " + o.getClass().getName() + "values: "+ o);
				return o.intValue() == 1;
			}
		};
		try {
			return (Boolean) hu.execute(hs);
		} catch (Exception e) {
			log.warn(e, e);
			return null;
		}
	}
}
