package com.mingchao.snsspider.task;

public class CloseRunnableTask extends RunnableTask implements CloseableTask {
	
		public CloseRunnableTask(CloseableTask task) {
			super(task);
		}
	
		@Override
		public void close(){
			((CloseableTask)task).close();
		}
}
