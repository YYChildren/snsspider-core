package com.mingchao.snsspider.storage;

import com.mingchao.snsspider.storage.util.HibernateUtil;

public enum HibernateUtilLocal {
	INSTANCE;
	private final HibernateUtil instance;

	private HibernateUtilLocal() {
		instance = new HibernateUtil();
	}

	public HibernateUtil getInstance() {
		return instance;
	}
}