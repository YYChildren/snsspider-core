package com.mingchao.snsspider.storage;

import com.mingchao.snsspider.storage.spi.hadoop.StorageHadoop;

public class StorageHadoopSington {

	private static Storage instance;
	
	public static Storage getIntance() {
		if(instance == null){
			init();
		}
		return instance;
	}
	private static synchronized void init() {
		if(instance == null){
			instance = new StorageImpl(new StorageHadoop());
		}
	}
}
