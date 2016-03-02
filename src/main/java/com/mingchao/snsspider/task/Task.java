package com.mingchao.snsspider.task;


public interface Task{
	void before();
	void execute() throws Exception;
	void after();
}
