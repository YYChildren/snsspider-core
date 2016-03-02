package com.mingchao.snsspider.http;


public class CookieStorePoolProvider{
	private static CookieStorePool instance;

	public static CookieStorePool getInstance() {
		if (instance == null) {
			initTaskExcutor();
		}
		return instance;
	}

	private static synchronized void initTaskExcutor() {
		if (instance == null) {
			instance = new CookieStorePool();
		}
	}
}
