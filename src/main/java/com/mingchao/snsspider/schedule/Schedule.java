package com.mingchao.snsspider.schedule;

import java.util.List;

import com.mingchao.snsspider.model.ToBytes;

public interface Schedule <T extends ToBytes>{
	
	boolean containsKey(T e);

	void schadule(List<T> list);
	
	void schadule(T e);

	void reschadule(T e);

	T fetch();
}
