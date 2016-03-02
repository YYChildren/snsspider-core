package com.mingchao.snsspider.schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.persistence.Entity;

import com.mingchao.snsspider.model.QueueStatus;
import com.mingchao.snsspider.model.ToBytes;
import com.mingchao.snsspider.model.IdAble;
import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.storage.StorageJdbcSington;
import com.mingchao.snsspider.util.BloomFilter;

public class ScheduleJdbc <T extends ToBytes> extends ScheduleImpl <T>{

	protected static long EXPECTEDENTRIES = 50000000;
	protected static int STEP = 1000;
	protected Queue<T> queue;
	protected BloomFilter filter;
	protected Storage storage;
	protected boolean waiting; 
	protected Class<T> entryClass;
	protected String entryKey;

	public ScheduleJdbc(Class<T> entryClass){
		this(entryClass,EXPECTEDENTRIES);
	}
	
	public ScheduleJdbc(Class<T> entryClass,long expectedEntries) {
		queue = new LinkedList<T>();
		filter = new BloomFilter(expectedEntries);
		waiting = false; 
		this.entryClass = entryClass;
		this.entryKey =  getTableName(entryClass);
		setStorage();
	}
	
	protected void setStorage(){
		this.storage = StorageJdbcSington.getInstance();
	}
	
	public synchronized boolean containsKey(T e){
		byte[] bytes = e.toBytes();
		return filter.test(bytes);
	}

	public synchronized void schadule(List<T> list) {
		boolean hasNew = false;
		List<T> list2 = new ArrayList<T>();
		for (T e : list) {
			byte[] bytes = e.toBytes();
			if (!filter.test(bytes)) {
				list2.add(e);
				hasNew = true;
			}
		}
		storage.insertIgnore(list2);
		if(hasNew && waiting){
			waiting = false;
			notify();
		}
	}
	
	public synchronized void schadule(T e) {
		boolean hasNew = false;
		byte[] bytes = e.toBytes();
		if (!filter.test(bytes)) {
			storage.insertIgnore(e);
			hasNew = true;
		}
		if(hasNew && waiting){
			waiting = false;
			notify();
		}
	}

	public synchronized void reschadule(T e) {
		byte[] bytes = e.toBytes();
		filter.add(bytes);
		storage.insertIgnore(e);
		if(waiting){
			waiting = false;
			notify();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized T fetch() {
		T e;
		while(true){
			log.info(queue.getClass().getSimpleName() + " size: " + queue.size());
			e = (T) queue.poll();
			if(e == null){
				QueueStatus queueStatus = (QueueStatus) storage.get(QueueStatus.class, entryKey);//获取进度
				if(queueStatus == null){
					queueStatus = new QueueStatus();
					queueStatus.setTableName(entryKey);
					queueStatus.setIdStart(1L);
				}
				Long idStart = queueStatus.getIdStart();
				Long idEnd = idStart + STEP;
				List<Object>  list = storage.get(entryClass, idStart, idEnd);
				log.info("idStart: "+ idStart);
				if(!(list == null || list.isEmpty())){
					for (Object newEntry : list) {
						queue.offer((T)newEntry);
					}
					idEnd = ((IdAble)list.get(list.size() - 1)).getId() + 1;
					storage.delete(entryClass, 1, idStart);//删除旧队列
					queueStatus.setIdStart(idEnd);
					storage.insertDuplicate(queueStatus);//更新进度
					continue;
				}else{
					log.info(entryClass + " between " + idStart + " and " +  idEnd + " is null");
					if(storage.hasMore(entryClass, idEnd)){
						storage.insertDuplicate(queueStatus);//更新进度
						continue;
					}
					waiting = true;
					try {
						wait();
					} catch (InterruptedException e1) {
						break;
					}
				}
			}else{
				break;
			}
		}
		return e;
	}
	
	private String getTableName(Class<?> clazz){
		if(clazz == null){
			return null;
		}
		String table = null;
		try {
			 table = clazz.getAnnotation(Entity.class).name();
			 table = table.equals("") ? clazz.getSimpleName().toLowerCase(): table;
		} catch (NullPointerException e) {
			table = clazz.getSimpleName().toLowerCase();
		}
		return table;
	}
}
