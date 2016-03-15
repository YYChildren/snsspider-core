package com.mingchao.snsspider.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.model.IdAble;
import com.mingchao.snsspider.model.QueueStatus;
import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.storage.StorageJdbcLocal;
import com.mingchao.snsspider.storage.util.SQLUtil;
import com.mingchao.snsspider.util.BloomFilterUtil;

public class ScheduleJdbc<T  extends IdAble> extends ScheduleImpl<T> {
	
	protected static int STEP = 1000;
	protected Queue<T> queue;
	protected Storage storage;
	protected boolean waiting;
	protected String entryTable;
	private Class<? extends QueueStatus> queueStatusClass; 
	
	public ScheduleJdbc(Class<T> entryClass, Class<? extends QueueStatus> queueStatusClass, Funnel<T> funnel) {
		this(entryClass, queueStatusClass, funnel, BloomFilterUtil.EXPECTEDENTRIES);
	}

	public ScheduleJdbc(Class<T> entryClass, Class<? extends QueueStatus> queueStatusClass, Funnel<T> funnel, long expectedEntries) {
		this(entryClass, queueStatusClass, funnel, expectedEntries, BloomFilterUtil.DEFAULT_FPP);
	}

	public ScheduleJdbc(Class<T> entryClass, Class<? extends QueueStatus> queueStatusClass,  Funnel<T> funnel, long expectedEntries, double fpp) {
		this(entryClass, queueStatusClass, funnel, expectedEntries, fpp, null);
	}

	public ScheduleJdbc(Class<T> entryClass, Class<? extends QueueStatus> queueStatusClass,  Funnel<T> funnel, long expectedEntries, double fpp,
			String bloomPath) {
		queue = new ConcurrentLinkedQueue<T>();
		this.entryClass = entryClass;
		this.entryTable = SQLUtil.getTableName(entryClass);
		this.queueStatusClass = queueStatusClass;
		this.bloomPath = bloomPath;
		this.filter = readFrom(funnel);
		this.filter = filter == null ? BloomFilter.create(funnel, expectedEntries, fpp) : filter; 
		waiting = false;
		setStorage();
	}

	protected void setStorage() {
		this.storage = StorageJdbcLocal.JDBC.getInstance();
	}

	public synchronized boolean containsKey(T e) {
		return filter.mightContain(e);
	}

	public synchronized void schadule(List<T> list) {
		boolean hasNew = false;
		List<T> list2 = new ArrayList<T>();
		for (T e : list) {
			if (!filter.mightContain(e)) {
				filter.put(e);
				list2.add(e);
				hasNew = true;
			}
		}
		storage.insertIgnore(list2);
		if (hasNew && waiting) {
			waiting = false;
			notify();
		}
	}

	public synchronized void schadule(T e) {
		boolean hasNew = false;
		if (!filter.mightContain(e)) {
			filter.put(e);
			storage.insertIgnore(e);
			hasNew = true;
		}
		if (hasNew && waiting){
			waiting = false;
			notify();
		}
	}

	public synchronized void reschadule(T e) {
		filter.put(e);
		storage.insertIgnore(e);
		if (waiting) {
			waiting = false;
			notify();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized T fetch() {
		if(closing){
			return null;
		}
		log.debug(entryClass.getSimpleName() + " size: " + queue.size());
		T e = null;
		QueueStatus queueStatus = null;
		if(queue.isEmpty()) {
			queueStatus = (QueueStatus) storage.get(queueStatusClass, entryTable);// 获取进度
			if (queueStatus == null) {
				try {
					queueStatus = queueStatusClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e1) {//不会出现该错误
					log.warn(e1, e1);
				}
				queueStatus.setTableName(entryTable);
				queueStatus.setIdEnd(1L);
			}
		}
		while (queue.isEmpty()) {
			Long idStart = queueStatus.getIdEnd();//小于idStart的可以删除
			Long idEnd = idStart + STEP;
			queueStatus.setIdStart(idStart);
			List<Object> list = storage.get(entryClass, idStart, idEnd);
			log.debug("idStart: " + idStart);
			if (list == null || list.isEmpty()) {
				log.debug(entryClass + " between " + idStart + " and "
						+ idEnd + " is null");
				if (storage.hasMore(entryClass, idEnd)) {
					log.debug("has more id: " + idEnd);
					queueStatus.setIdStart(idEnd);
					continue;
				}
				log.debug("has no more id: " + idEnd);
				waiting = true;
				try {
					wait();
				} catch (InterruptedException e1) {
					throw new NPInterruptedException(e1);
				}
			} else {
				for (Object newEntry : list) {
					queue.offer((T) newEntry);
				}
				idEnd = ((IdAble) list.get(list.size() - 1)).getId() + 1;
				storage.delete(entryClass, 1, idStart);// 删除旧队列
				queueStatus.setIdStart(idEnd);
				storage.insertDuplicate(queueStatus);// 更新进度
			}
		}
		e = (T) queue.poll();
		return e;
	}
	
	@Override
	public void dump() {
		super.dump();
		T e = queue.peek();
		if(e != null){
			Long endId = e.getId();
			QueueStatus queueStatus;
			try {
				queueStatus = queueStatusClass.newInstance();
				queueStatus.setTableName(entryTable);
				queueStatus.setIdEnd(endId);
				storage.insertDuplicate(queueStatus);
			} catch (InstantiationException | IllegalAccessException e1) {//不会出现该错误
				log.warn(e1, e1);
			}
		}
	}
	
	@Override
	public void close(){
		super.close();
		storage.close();
	}
}
