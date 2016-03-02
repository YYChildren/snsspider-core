package com.mingchao.snsspider.util;

public class TimeUtils {
	public static final long TIMEOUT = 5000;
	public static void sleep(){
		sleep(TIMEOUT);
	}
	public static void sleep(long timeout){
		try {
			Thread.sleep(TIMEOUT);
		} catch (InterruptedException e) {
		}
	}
}
