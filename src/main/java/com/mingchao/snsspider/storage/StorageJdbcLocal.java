package com.mingchao.snsspider.storage;

import com.mingchao.snsspider.storage.jdbc.StorageJdbc;
import com.mingchao.snsspider.storage.jdbc.StorageMySQL;
import com.mingchao.snsspider.storage.util.HibernateUtil;

public enum StorageJdbcLocal {
	JDBC(HibernateUtilLocal.INSTANCE.getInstance()), MySQL(
			HibernateUtilLocal.INSTANCE.getInstance(), true);
	private final Storage instance;

	private StorageJdbcLocal(HibernateUtil hu) {
		this(hu, false);
	}

	private StorageJdbcLocal(HibernateUtil hu, boolean isMySQL) {
		instance = isMySQL ? new StorageMySQL(hu) : new StorageJdbc(hu);
	}

	public Storage getInstance() {
		return instance;
	}
}

