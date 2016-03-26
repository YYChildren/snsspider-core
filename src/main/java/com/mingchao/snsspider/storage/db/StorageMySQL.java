package com.mingchao.snsspider.storage.db;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.mingchao.snsspider.storage.util.HibernateExeTask;
import com.mingchao.snsspider.storage.util.HibernateUtil;
import com.mingchao.snsspider.storage.util.SQLUtil;

public class StorageMySQL extends StorageJdbc {

	public StorageMySQL(HibernateUtil hu) {
		super(hu);
	}

	public void insertIgnore(Object object) {
		final String sql = SQLUtil.getInsertIgnoreSql(object);
		if (sql == null) {
			return;
		}
		HibernateExeTask hs = new HibernateExeTask() {
			@Override
			public Object execute(Session session) {
				session.createSQLQuery(sql).executeUpdate();
				return null;
			}
		};
		hu.execute(hs);
	}

	public void insertIgnore(final List<?> list) {
		HibernateExeTask hs = new HibernateExeTask() {
			@Override
			public Object execute(Session session) {
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					String sql = SQLUtil.getInsertIgnoreSql(object);
					if (sql == null) {
						continue;
					}
					session.createSQLQuery(sql).executeUpdate();
				}
				return null;
			}
		};
		hu.execute(hs);
	}

	public void insertDuplicateAutoIncrement(Object object) {
		final String sql = SQLUtil.getInsertDuplicateSql(object);
		if (sql == null) {
			return;
		}
		HibernateExeTask hs = new HibernateExeTask() {
			@Override
			public Object execute(Session session) {
				session.createSQLQuery(sql).executeUpdate();
				return null;
			}
		};
		hu.execute(hs);
	}

	public void insertDuplicate(Object object) {
		final String sql = SQLUtil.getInsertDuplicateSql(object);
		if (sql == null) {
			return;
		}
		HibernateExeTask hs = new HibernateExeTask() {
			@Override
			public Object execute(Session session) {
				session.createSQLQuery(sql).executeUpdate();
				return null;
			}
		};
		hu.execute(hs);
	}

	public void insertDuplicateAutoId(Object object) {
		final String sql = SQLUtil.getInsertDuplicateAutoIdSql(object);
		if (sql == null) {
			return;
		}
		HibernateExeTask hs = new HibernateExeTask() {
			@Override
			public Object execute(Session session) {
				session.createSQLQuery(sql).executeUpdate();
				return null;
			}
		};
		hu.execute(hs);
	}

	public void insertDuplicate(final List<?> list) {
		HibernateExeTask hs = new HibernateExeTask() {
			@Override
			public Object execute(Session session) {
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					String sql = SQLUtil.getInsertDuplicateSql(object);
					if (sql == null) {
						continue;
					}
					session.createSQLQuery(sql).executeUpdate();
				}
				return null;
			}
		};
		hu.execute(hs);
	}

}
