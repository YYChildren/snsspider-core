package com.mingchao.snsspider.storage;

import com.mingchao.snsspider.storage.util.HibernateUtil;

public enum HibernateUtilMaster{
	INATNCE("master.cfg.xml");
	private final HibernateUtil instance;
	
	private HibernateUtilMaster(String resource) {
		instance = new  HibernateUtil(resource);
	}

	public HibernateUtil getInstance() {
		return instance;
	}
}