package com.mingchao.snsspider.schedule;

import java.util.List;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.model.ToBytes;

public abstract class ScheduleImpl<T extends ToBytes> implements Schedule<T> {
	
	protected Log log = LogFactory.getLog(this.getClass());

	@Override
	public boolean containsKey(T e) {
		return false;
	}

	@Override
	public void schadule(List<T> list) {
	}

	@Override
	public void schadule(T e) {
	}

	@Override
	public void reschadule(T e) {
	}

	@Override
	public T fetch() {
		return null;
	}

}
