package com.mingchao.snsspider.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseTask implements Task{
	protected Log log = LogFactory.getLog(this.getClass()); 
	
	@Override
	public void before() {
	}

	@Override
	public void execute(){
	}

	@Override
	public void after() {
	}
}
