package com.mingchao.snsspider.main;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface SpiderService extends Remote{
	void close()  throws RemoteException;
}
