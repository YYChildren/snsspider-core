package com.mingchao.snsspider.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.mingchao.snsspider.util.Closeable;

public class TestSpiderServiceCtl {
	public static void main(String[] args) {
		Closeable closeable = new Closeable() {
			@Override
			public void close() {
				System.out.println("test ok");
			}
		};
		int port = 7777;
		try {
			SpiderServiceCtl.startServer(closeable, port);
			Thread.sleep(200);
			SpiderServiceCtl.close(port);
		} catch (RemoteException | MalformedURLException | NotBoundException
				| InterruptedException e) {
			e.printStackTrace();
		}
	}
}
