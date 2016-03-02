package com.mingchao.snsspider.task;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;

public class BaseTask implements Task{
	protected Log log = LogFactory.getLog(this.getClass()); 
	
	@Override
	public void before() {
	}

	@Override
	public void execute() throws Exception {
	}

	@Override
	public void after() {
	}
}
