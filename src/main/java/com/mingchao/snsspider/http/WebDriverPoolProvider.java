package com.mingchao.snsspider.http;

public class WebDriverPoolProvider {
	private static WebDriverPool pool;
	
	public static WebDriverPool getInstance() {
		if (pool == null) {
			initPool();
		}
		return pool;
	}

	private static synchronized void initPool() {
		if (pool == null) {
			pool = new WebDriverPool();
		}
	}
}
