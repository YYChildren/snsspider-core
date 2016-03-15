package com.mingchao.snsspider.storage.util;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;

public class HibernateUtil {
	
	private Log log = LogFactory.getLog(this.getClass());

	private final SessionFactory sessionFactory;

	public HibernateUtil() {
		this(StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME); 
	}
	
	public HibernateUtil(String resource) {
		try {
			sessionFactory = new Configuration().configure(resource).buildSessionFactory();
		} catch (Throwable ex) {
			log.error("Initial SessionFactory creation failed." , ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void save(final Object data) throws Exception{
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				session.save(data);
				return null;
			}
		};
		execute(hs);
    }

	public void saveOrUpdate(final Object data) throws Exception{
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				session.saveOrUpdate(data);return null;
			}
		};
		execute(hs);
    }

	public Object get(final Class<?> c,final Serializable id) throws Exception {
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				return session.get(c, id);
			}
		}; 
		return execute(hs);
	}

	public Object load(final Class<?> c,final Serializable id) throws Exception {
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				return session.load(c, id);
			}
		};
		return execute(hs);
	}

	public void delete(final Object o) throws Exception{
		HibernateSql hs = new HibernateSql(){  
			@Override
			public Object execute(Session session) throws Exception {
				session.delete(o);
				return null;
			}
		}; 
		execute(hs);
	}

	public Object execute(HibernateSql hs) throws Exception {
		Session session = null;
		try {
			session = sessionFactory.openSession();
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
	
	public void close(){
		sessionFactory.close();
	}
}