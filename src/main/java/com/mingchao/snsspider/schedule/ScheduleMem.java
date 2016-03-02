package com.mingchao.snsspider.schedule;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mingchao.snsspider.model.ToBytes;
import com.mingchao.snsspider.util.BloomFilter;

public class ScheduleMem <T extends ToBytes> extends ScheduleImpl <T>{

	private static long EXPECTEDENTRIES = 50000000;
	private Queue<ToBytes> queue;
	private BloomFilter filter;

	public ScheduleMem(){
		this(EXPECTEDENTRIES);
	}
	
	public ScheduleMem(long expectedEntries) {
		queue = new LinkedList<ToBytes>();
		filter = new BloomFilter(expectedEntries);
	}
	
	public synchronized boolean containsKey(T e){
		byte[] bytes = e.toBytes();
		return filter.test(bytes);
	}

	public synchronized void schadule(List<T> list) {
		for (T userKey : list) {
			schadule(userKey);
		}
	}
	
	public synchronized void schadule(T e) {
		byte[] bytes = e.toBytes();
		if (!filter.test(bytes)) {
			filter.add(bytes);
			queue.offer(e);
		}
	}

	public synchronized void reschadule(T e) {
			byte[] bytes = e.toBytes();
			filter.add(bytes);
			queue.offer(e);
	}

	@SuppressWarnings("unchecked")
	public synchronized T fetch() {
			return (T) queue.poll();
	}
}
