package com.mingchao.snsspider.schedule;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.mingchao.snsspider.util.BloomFilterUtil;

public class ScheduleMem <T> extends ScheduleImpl <T>{

	private Queue<T> queue;

	public ScheduleMem(Class<T> entryClass,  Funnel<T> funnel) {
		this(entryClass, funnel, BloomFilterUtil.EXPECTEDENTRIES);
	}

	public ScheduleMem(Class<T> entryClass, Funnel<T> funnel, long expectedEntries) {
		this(entryClass, funnel, expectedEntries, BloomFilterUtil.DEFAULT_FPP);
	}

	public ScheduleMem(Class<T> entryClass,  Funnel<T> funnel, long expectedEntries, double fpp) {
		this(entryClass, funnel, expectedEntries, fpp, null);
	}

	public ScheduleMem(Class<T> entryClass,  Funnel<T> funnel, long expectedEntries, double fpp,
			String bloomPath) {
		this.entryClass = entryClass;
		this.bloomPath = bloomPath;
		queue = new LinkedList<T>();
		this.filter = BloomFilter.create(funnel, expectedEntries, fpp);
	}
	
	
	public synchronized boolean containsKey(T e){
		return filter.mightContain(e);
	}

	public synchronized void schadule(List<T> list) {
		for (T userKey : list) {
			schadule(userKey);
		}
	}
	
	public synchronized void schadule(T e) {
		if (!filter.mightContain(e)) {
			filter.put(e);
			queue.offer(e);
		}
	}

	public synchronized void reschadule(T e) {
			filter.put(e);
			queue.offer(e);
	}

	public synchronized T fetch() {
			return (T) queue.poll();
	}
}
