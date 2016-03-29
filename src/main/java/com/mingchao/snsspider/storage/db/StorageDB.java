package com.mingchao.snsspider.storage.db;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.mingchao.snsspider.storage.StorageAdaptor;
import com.mingchao.snsspider.storage.util.HibernateExeTask;
import com.mingchao.snsspider.storage.util.HibernateUtil;

public class StorageDB extends StorageAdaptor {
	protected HibernateUtil hu;

	public StorageDB(HibernateUtil hu) {
		this.hu = hu;
	}

	public void insert(Object object) {
		hu.save(object);
	}

	public void insert(final List<?> list) {
		HibernateExeTask hs = new HibernateExeTask() {
			@Override
			public Object execute(Session session) {
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					if(object != null){
						session.save(object);
					}
				}
				return null;
			}
		};
		hu.execute(hs);
	}

	public void insertIgnore(Object object) {
		insert(object);
	}

	public void insertIgnore(List<?> list) {
		insert(list);
	}

	public void insertDuplicate(Object object) {
		hu.saveOrUpdate(object);
	}

	public void insertDuplicate(final List<?> list) {
		HibernateExeTask hs = new HibernateExeTask() {
			@Override
			public Object execute(Session session) {
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					if(object != null){
						session.saveOrUpdate(object);
					}
				}
				return null;
			}
		};
		hu.execute(hs);
	}

	@Override
	public Object get(Class<?> c, Serializable id) {
		return hu.get(c, id);
	}

	public Object execute(HibernateExeTask hs){
		return hu.execute(hs);
	}

	@Override
	public void close() {
		hu.close();
	}
}
