package com.mingchao.snsspider.main;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.mingchao.snsspider.exception.SpiderLuncherException;
import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.util.Closeable;

public class SpiderRmiLuncher  implements SpiderLuncher {
	private final String BASENAME =  "SpiderService";
	private final String SHORTNAME_FORMAT = BASENAME + "-%d";
	private final String LONGNAME_FORMAT = "rmi://127.0.0.1:%d/" + SHORTNAME_FORMAT; 
	private final Log log = LogFactory.getLog(this.getClass());
	private final String shortName;
	private final String longName;
	private Closeable closeable;
	private Registry reg;
	
	private int port;
	
	/**
	 *  客户端调用
	 * @param port
	 */
	public SpiderRmiLuncher(int port){
		this(null,port);
	}
	
	/** 
	 * 服务端调用
	 * @param closeable
	 * @param port
	 */
	public SpiderRmiLuncher(Closeable closeable, int port){
		this.closeable = closeable;
		this.port = port;
		shortName = String.format(SHORTNAME_FORMAT, port);
		longName = String.format(LONGNAME_FORMAT, port, port);
	}

	// 服务端调用
	@Override
	public void startServer(){
		try {
			SpiderService spiderService = new SpiderServiceImpl(closeable);
			// 注册通讯端口
			reg = LocateRegistry.createRegistry(port);
			// 注册通讯路径
			Naming.rebind(longName, spiderService);
			log.warn("Service Start!");
		} catch (RemoteException | MalformedURLException  e) {
			throw new SpiderLuncherException(e);
		}
		
	}
	
	// 服务端调用
	@Override
	public void stopServer(){
		try {
			reg.unbind(shortName);
			UnicastRemoteObject.unexportObject(reg, true);
		} catch (RemoteException | NotBoundException e) {
			throw new SpiderLuncherException(e);
		}
	}
	
	//客户端调用
	@Override
	public void close(){
		// 调用远程对象，注意RMI路径与接口必须与服务器配置一致
		SpiderService spiderService;
		try {
			spiderService = (SpiderService) Naming.lookup(longName);
			spiderService.close();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			throw new SpiderLuncherException(e);
		}
	}
}

interface SpiderService extends Remote {
	void close() throws RemoteException;
}

class SpiderServiceImpl extends UnicastRemoteObject implements SpiderService {

	private static final long serialVersionUID = -481626812374792151L;
	private Closeable closeable;

	public SpiderServiceImpl(Closeable closeable) throws RemoteException {
		this.closeable = closeable;
	}

	@Override
	public void close() throws RemoteException {
		closeable.close();
	}

}
