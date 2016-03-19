package com.mingchao.snsspider.main;

import com.mingchao.snsspider.util.Closeable;

public class TestSpiderJmxLuncher {
	public static void main(String[] args) {
		Closeable closeable = new Closeable() {
			@Override
			public void close() {
				System.out.println("test ok");
			}
		};
		SpiderJmxLuncher lun = null;
		try {
			lun = new SpiderJmxLuncher(closeable);
			lun.startServer();
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(lun != null){
				lun.close();
			}
		}
	}
}
