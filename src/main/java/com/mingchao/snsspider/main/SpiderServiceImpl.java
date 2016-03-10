package com.mingchao.snsspider.main;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.mingchao.snsspider.util.Closeable;

class SpiderServiceImpl  extends UnicastRemoteObject implements SpiderService {
	
	private static final long serialVersionUID = -481626812374792151L;
	private Closeable closeable;

	public SpiderServiceImpl(Closeable closeable) throws RemoteException{
		this.closeable = closeable;
	}

	@Override
	public void close()  throws RemoteException{
		closeable.close();
	}

}
