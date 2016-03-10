package com.mingchao.snsspider.schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.persistence.Entity;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.mingchao.snsspider.exception.NPInterruptedException;
import com.mingchao.snsspider.model.IdAble;
import com.mingchao.snsspider.model.QueueStatus;
import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.storage.StorageJdbcSington;
import com.mingchao.snsspider.util.BloomFilterUtil;

public class ScheduleJdbc<T  extends IdAble> extends ScheduleImpl<T> {
	
	protected static int STEP = 1000;
	protected Queue<T> queue;
	protected Storage storage;
	protected boolean waiting;
	protected String entryKey;
	
	public ScheduleJdbc(Class<T> entryClass,  Funnel<T> funnel) {
		this(entryClass, funnel, BloomFilterUtil.EXPECTEDENTRIES);
	}

	public ScheduleJdbc(Class<T> entryClass, Funnel<T> funnel, long expectedEntries) {
		this(entryClass, funnel, expectedEntries, BloomFilterUtil.DEFAULT_FPP);
	}

	public ScheduleJdbc(Class<T> entryClass,  Funnel<T> funnel, long expectedEntries, double fpp) {
		this(entryClass, funnel, expectedEntries, fpp, null);
	}

	public ScheduleJdbc(Class<T> entryClass,  Funnel<T> funnel, long expectedEntries, double fpp,
			String bloomPath) {
		queue = new LinkedList<T>();
		this.entryClass = entryClass;
		this.entryKey = getTableName(entryClass);
		this.bloomPath = bloomPath;
		this.filter = BloomFilter.create(funnel, expectedEntries, fpp);
		waiting = false;
		setStorage();
	}

	protected void setStorage() {
		this.storage = StorageJdbcSington.getInstance();
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
		}else{
			log.debug("not schedule: " + e.getClass() + ":" + e.toString());
		}
		if (hasNew && waiting) {
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
		T e;
		while (true) {
			log.info(entryClass.getSimpleName() + " size: " + queue.size());
			e = (T) queue.poll();
			if (e == null) {
				QueueStatus queueStatus = (QueueStatus) storage.get(
						QueueStatus.class, entryKey);// 获取进度
				if (queueStatus == null) {
					queueStatus = new QueueStatus();
					queueStatus.setTableName(entryKey);
					queueStatus.setIdStart(1L);
				}
				Long idStart = queueStatus.getIdStart();
				Long idEnd = idStart + STEP;
				List<Object> list = storage.get(entryClass, idStart, idEnd);
				log.debug("idStart: " + idStart);
				if (!(list == null || list.isEmpty())) {
					for (Object newEntry : list) {
						queue.offer((T) newEntry);
					}
					idEnd = ((IdAble) list.get(list.size() - 1)).getId() + 1;
					storage.delete(entryClass, 1, idStart);// 删除旧队列
					queueStatus.setIdStart(idEnd);
					storage.insertDuplicate(queueStatus);// 更新进度
					continue;
				} else {
					log.debug(entryClass + " between " + idStart + " and "
							+ idEnd + " is null");
					if (storage.hasMore(entryClass, idEnd)) {
						log.debug("has more id: " + idEnd);
						queueStatus.setIdStart(idEnd);
						storage.insertDuplicate(queueStatus);// 更新进度
						continue;
					}
					log.debug("has no more id: " + idEnd);
					waiting = true;
					try {
						wait();
					} catch (InterruptedException e1) {
						throw new NPInterruptedException(e1);
					}
				}
			} else {
				break;
			}
		}
		return e;
	}

	private String getTableName(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		String table = null;
		try {
			table = clazz.getAnnotation(Entity.class).name();
			table = table.equals("") ? clazz.getSimpleName().toLowerCase()
					: table;
		} catch (NullPointerException e) {
			table = clazz.getSimpleName().toLowerCase();
		}
		return table;
	}

}
