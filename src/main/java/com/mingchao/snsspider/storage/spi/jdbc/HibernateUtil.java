package com.mingchao.snsspider.storage.spi.jdbc;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;

public class HibernateUtil {
	
	private static Log log = LogFactory.getLog(HibernateUtil.class);

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			log.error("Initial SessionFactory creation failed." , ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void save(final Object data) throws Exception{
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				session.save(data);
				return null;
			}
		};
		execute(hs);
    }

	public static void saveOrUpdate(final Object data) throws Exception{
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				session.saveOrUpdate(data);return null;
			}
		};
		execute(hs);
    }

	public static Object get(final Class<?> c,final Serializable id) throws Exception {
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				return session.get(c, id);
			}
		}; 
		return execute(hs);
	}

	public static Object load(final Class<?> c,final Serializable id) throws Exception {
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				return session.load(c, id);
			}
		};
		return execute(hs);
	}

	public static void delete(final Object o) throws Exception{
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				session.delete(o);
				return null;
			}
		}; 
		execute(hs);
	}

	public static Object execute(HibernateSql hs) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Object result = hs.execute(session);
			session.getTransaction().commit();
			return result;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public static void close(){
		sessionFactory.close();
	}
}