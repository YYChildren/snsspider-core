package com.mingchao.snsspider.storage.util;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class HibernateUtil {
	
	protected Log log = LogFactory.getLog(this.getClass());

	protected SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void save(final Object data){
		HibernateExeTask hs = new HibernateExeTask(){  
			@Override
			public Object execute(Session session){
				session.save(data);
				return null;
			}
		};
		execute(hs);
    }

	public void saveOrUpdate(final Object data){
		HibernateExeTask hs = new HibernateExeTask(){  
			@Override
			public Object execute(Session session){
				session.saveOrUpdate(data);return null;
			}
		};
		execute(hs);
    }

	public Object get(final Class<?> c,final Serializable id){
		HibernateExeTask hs = new HibernateExeTask(){  
			@Override
			public Object execute(Session session){
				return session.get(c, id);
			}
		}; 
		return execute(hs);
	}

	public Object load(final Class<?> c,final Serializable id){
		HibernateExeTask hs = new HibernateExeTask(){  
			@Override
			public Object execute(Session session){
				return session.load(c, id);
			}
		};
		return execute(hs);
	}

	public void delete(final Object o){
		HibernateExeTask hs = new HibernateExeTask(){  
			@Override
			public Object execute(Session session){
				session.delete(o);
				return null;
			}
		}; 
		execute(hs);
	}

	public Object execute(HibernateExeTask hs){
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			Object result = hs.execute(session);
			session.getTransaction().commit();
			return result;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			if(session!=null){
				session.close();
			}
		}
	}
	
	public void close(){
		sessionFactory.close();
	}
}