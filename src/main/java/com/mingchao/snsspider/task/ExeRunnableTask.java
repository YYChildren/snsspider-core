package com.mingchao.snsspider.task;

import com.mingchao.snsspider.manager.TaskExcutor;

public class ExeRunnableTask extends RunnableTask{
	protected TaskExcutor taskExecutor;

	public ExeRunnableTask(Task task, TaskExcutor taskExecutor) {
		super(task);
		this.taskExecutor = taskExecutor;
	}
	
	public TaskExcutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExcutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public void after() {
		super.after();
		if (taskExecutor != null) {
			taskExecutor.after();
		}
	}
}
