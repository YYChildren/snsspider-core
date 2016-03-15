package com.mingchao.snsspider.storage;

import com.mingchao.snsspider.storage.jdbc.StorageMySQL;
import com.mingchao.snsspider.storage.util.HibernateUtil;

public enum StorageMySQLMaster {

	INSTANCE(HibernateUtilMaster.INATNCE.getInstance());
	private final StorageMySQL instance;

	private StorageMySQLMaster(HibernateUtil hu) {
		instance = new StorageMySQL(hu);
	}

	public StorageMySQL getInstance() {
		return instance;
	}
}