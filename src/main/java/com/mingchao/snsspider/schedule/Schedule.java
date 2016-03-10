package com.mingchao.snsspider.schedule;

import java.util.List;

public interface Schedule <T>{
	
	boolean containsKey(T e);

	void schadule(List<T> list);
	
	void schadule(T e);

	void reschadule(T e);

	T fetch();
	
	void close();
}
