package com.mingchao.snsspider.util;

public class TimeUtils {
	public static final long TIMEOUT = 5000;

	public static void sleep() throws InterruptedException {
		sleep(TIMEOUT);
	}

	public static void sleep(long timeout) throws InterruptedException {
		Thread.sleep(TIMEOUT);
	}
}
