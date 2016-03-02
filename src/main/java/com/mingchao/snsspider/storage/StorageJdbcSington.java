package com.mingchao.snsspider.storage;

import com.mingchao.snsspider.storage.spi.jdbc.StorageJdbc;

public class StorageJdbcSington {
	private static Storage instance; 
	
	public static Storage getInstance() {
		if(instance == null){
			initJdbcStorage();
		}
		return instance;
	}
	

	private static synchronized void initJdbcStorage() {
		if(instance == null){
			instance = new StorageImpl(new StorageJdbc());
		}
	}
}
