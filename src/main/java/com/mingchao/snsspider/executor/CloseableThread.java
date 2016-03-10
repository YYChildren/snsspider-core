package com.mingchao.snsspider.executor;

import com.mingchao.snsspider.task.CloseRunnableTask;
import com.mingchao.snsspider.util.Closeable;

public class CloseableThread extends Thread implements Closeable{

	private CloseRunnableTask task;

	public CloseableThread(CloseRunnableTask task) {
		super(task);
		this.task = task;
	}

	public CloseableThread(CloseRunnableTask task, String name) {
		super(task, name);
		this.task = task;
	}

	public CloseableThread(ThreadGroup group, CloseRunnableTask task) {
		super(group, task);
		this.task = task;
	}
	
	public CloseableThread(ThreadGroup group, CloseRunnableTask task, String name) {
		super(group, task, name);
		this.task = task;
	}
	
	public CloseableThread(ThreadGroup group, CloseRunnableTask task, String name,
			long stackSize) {
		super(group, task, name, stackSize);
		this.task = task;
	}
	
	public CloseRunnableTask getTask() {
		return task;
	}

	@Override
	public void close() {
		task.close();
		this.interrupt();
	}
}
