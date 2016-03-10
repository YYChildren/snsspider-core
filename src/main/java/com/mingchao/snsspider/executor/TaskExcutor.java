package com.mingchao.snsspider.executor;

import com.mingchao.snsspider.task.Task;
import com.mingchao.snsspider.util.Closeable;

public interface TaskExcutor extends Closeable{
	void after();
	void execute(Task task) throws InterruptedException;
}