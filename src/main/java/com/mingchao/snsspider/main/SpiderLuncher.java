package com.mingchao.snsspider.main;


public interface SpiderLuncher {
	
	/**
	 * 服务端调用
	 * 启动服务器
	 */
	void startServer();

	/**
	 * 服务端最后调用
	 * 关闭服务器
	 */
	void stopServer();
	
	/**
	 * 客户端调用
	 * @return 0 为正常关闭， 1为非正常关闭
	 */
	void close();

}
