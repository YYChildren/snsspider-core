package com.mingchao.snsspider.storage;

import com.mingchao.snsspider.storage.spi.jdbc.StorageMySQL;

public class StorageMySQLSington {

	private static Storage instance; 

	public static Storage getInstance() {
		if(instance == null){
			init();
		}
		return instance;
	}
	
	private static synchronized void init() {
		if(instance == null){
			instance = new StorageImpl(new StorageMySQL());
		}
	}
	
}
