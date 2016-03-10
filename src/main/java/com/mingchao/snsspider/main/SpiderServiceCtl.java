package com.mingchao.snsspider.main;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.util.Closeable;

public class SpiderServiceCtl {

	private static Log log = LogFactory.getLog(SpiderServiceCtl.class);

	public static Registry startServer(Closeable closeable, int port)
			throws RemoteException, MalformedURLException, NotBoundException {
		SpiderService spiderService = new SpiderServiceImpl(closeable);
		// 注册通讯端口
		Registry reg = LocateRegistry.createRegistry(port);
		// 注册通讯路径
		Naming.rebind(getName(port),spiderService);
		log.info("Service Start!");
		return reg;
	}
	
	public static void stopServer(Registry reg) throws AccessException, RemoteException, NotBoundException{
		reg.unbind(getShortName());
        UnicastRemoteObject.unexportObject(reg, true);
	}

	// 另起客户端进程调用
	public static void close(int port) throws MalformedURLException,
			RemoteException, NotBoundException {
		// 调用远程对象，注意RMI路径与接口必须与服务器配置一致
		SpiderService spiderService = (SpiderService) Naming.lookup(getName(port));
		spiderService.close();
	}
	
	private static String getName(int port){
		String name = "rmi://127.0.0.1:%d/%s" ;
		return String.format(name, port, getShortName());
	}
	
	private static String getShortName(){
		return "SpiderService";
	}
}

