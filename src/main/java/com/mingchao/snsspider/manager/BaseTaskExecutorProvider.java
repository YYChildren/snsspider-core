package com.mingchao.snsspider.manager;


public class BaseTaskExecutorProvider{
	private static TaskExcutor instance;

	public static TaskExcutor getInstance() {
		if (instance == null) {
			initTaskExcutor();
		}
		return instance;
	}

	private static synchronized void initTaskExcutor() {
		if (instance == null) {
			instance = new BaseTaskExcutor();
		}
	}
}
