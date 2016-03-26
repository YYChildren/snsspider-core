package com.mingchao.snsspider.main;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.mingchao.snsspider.exception.SpiderLuncherException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mingchao.snsspider.util.Closeable;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class SpiderJmxLuncher implements SpiderLuncher {
	private final String CONNECTOR_ADDRESS = "com.sun.management.jmxremote.localConnectorAddress";
	private final Log log = LogFactory.getLog(this.getClass());
	private String pid;
	private Closeable closeable;
	private MBeanServer server;
	private ObjectName beanName;

	/**
	 * 服务端使用
	 * @param closeable 关闭接口
	 */
	public SpiderJmxLuncher(Closeable closeable) {
		this(null,closeable);
	}
	
	/**
	 * 客户端使用
	 * @param pid 服务端的进程Pid
	 */
	public SpiderJmxLuncher(String pid) {
		this(pid,null);
	}
	
	private SpiderJmxLuncher(String pid, Closeable closeable){
		if(pid == null || "".equals(pid)){
			pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];  
		}
		this.pid = pid;
		this.closeable = closeable;
		try {
			beanName = new ObjectName(SpiderJmx.class.getPackage().getName()
					+ ":type=" + SpiderJmx.class.getSimpleName());
		} catch (MalformedObjectNameException e) {// 不会发生此异常
			throw new SpiderLuncherException(e);
		}
	}

	// 服务端调用
	@Override
	public void startServer() {
		server = ManagementFactory.getPlatformMBeanServer();
		SpiderJmx spiderJmx = new SpiderJmx(closeable);
		try {
			server.registerMBean(spiderJmx, beanName);
			log.warn("Service Start!");
		} catch (InstanceAlreadyExistsException | MBeanRegistrationException
				| NotCompliantMBeanException e) {
			throw new SpiderLuncherException(e);
		}
	}

	// 服务端调用
	@Override
	public void stopServer() {
		try {
			server.unregisterMBean(beanName);
		} catch (MBeanRegistrationException | InstanceNotFoundException e) {
			throw new SpiderLuncherException(e);
		}
	}
	
	@Override
	public void close(){
		if(pid != null && !"".equals(pid)){
			closePid();
		}
	}

	private void closePid() {
		try {
			VirtualMachine vm = VirtualMachine.attach(pid);
			String connectorAddress = vm.getAgentProperties().getProperty(
					CONNECTOR_ADDRESS);
			if (connectorAddress == null) {
				String agent = vm.getSystemProperties()
						.getProperty("java.home")
						+ File.separator
						+ "lib"
						+ File.separator 
						+ "management-agent.jar";
				vm.loadAgent(agent);
				connectorAddress = vm.getAgentProperties().getProperty(
						CONNECTOR_ADDRESS);
			}
			JMXServiceURL url = new JMXServiceURL(connectorAddress);
			JMXConnector jmxConn = JMXConnectorFactory.connect(url);
			MBeanServerConnection mbsc = jmxConn.getMBeanServerConnection();
			close(mbsc);
		} catch (AttachNotSupportedException | IOException | AgentLoadException
				| AgentInitializationException | InstanceNotFoundException e) {
			throw new SpiderLuncherException(e);
		}
	}

	private void close(MBeanServerConnection mbsc)
			throws InstanceNotFoundException, IOException {
		SpiderJmxMBean proxy = MBeanServerInvocationHandler.newProxyInstance(
				mbsc, beanName, SpiderJmxMBean.class, false);
		proxy.close();
	}

}