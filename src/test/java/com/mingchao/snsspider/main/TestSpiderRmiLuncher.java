package com.mingchao.snsspider.main;

import com.mingchao.snsspider.util.Closeable;

public class TestSpiderRmiLuncher {
	public static void main(String[] args) {
		Closeable closeable = new Closeable() {
			@Override
			public void close() {
				System.out.println("test ok");
			}
		};
		int port = 7777;
		SpiderRmiLuncher lun = null;
		try {
			lun = new SpiderRmiLuncher(closeable, port);
			lun.startServer();
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(lun != null){
				lun.close();
			}
		}
		System.exit(0);
	}
}
