package com.mingchao.snsspider.schedule;

import java.util.List;

/**
 * 
 * @author yangchaojun
 *
 * @param <T> 需啊哟啊调度的结构
 */
public interface Schedule <T>{
	
	boolean containsKey(T e);

	void schadule(List<T> list);
	
	void schadule(T e);

	void reschadule(T e);

	T fetch();
	
	void closing();
	
	void dump();
	
	void close();
}
