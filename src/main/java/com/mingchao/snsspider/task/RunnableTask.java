package com.mingchao.snsspider.task;

import com.mingchao.snsspider.exception.NPInterruptedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RunnableTask implements Runnable, Task {
	protected Log log;
	protected Task task;

	public RunnableTask(Task task) {
		this.task = task;
		this.log = LogFactory.getLog(task.getClass());
	}
	
	public Task getTask() {
		return task;
	}

	@Override
	public void run() {
		try {
			before();
			execute();
		} catch(NPInterruptedException e){
			log.warn(e,e);
			Thread.currentThread().interrupt();
		}catch (Exception e) {
			log.warn(e,e);
		} finally {
			after();
		}
	}

	@Override
	public void before() {
		task.before();
	}

	@Override
	public void execute() throws Exception {
		task.execute();
	}

	@Override
	public void after() {
		task.after();
	}
}
