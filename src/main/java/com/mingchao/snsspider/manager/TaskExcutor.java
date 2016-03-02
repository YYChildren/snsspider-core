package com.mingchao.snsspider.manager;

import com.mingchao.snsspider.task.Task;

public interface TaskExcutor{
	void after();
	void execute(Task task);
}