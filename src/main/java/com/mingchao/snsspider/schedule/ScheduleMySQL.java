package com.mingchao.snsspider.schedule;

import com.google.common.hash.Funnel;
import com.mingchao.snsspider.model.IdAble;
import com.mingchao.snsspider.storage.StorageMySQLSington;
import com.mingchao.snsspider.util.BloomFilterUtil;

public class ScheduleMySQL <T extends IdAble> extends ScheduleJdbc<T>{
	public ScheduleMySQL(Class<T> entryClass,  Funnel<T> funnel) {
		super(entryClass, funnel, BloomFilterUtil.EXPECTEDENTRIES);
	}

	public ScheduleMySQL(Class<T> entryClass, Funnel<T> funnel, long expectedEntries) {
		super(entryClass, funnel, expectedEntries, BloomFilterUtil.DEFAULT_FPP);
	}

	public ScheduleMySQL(Class<T> entryClass,  Funnel<T> funnel, long expectedEntries, double fpp) {
		super(entryClass, funnel, expectedEntries, fpp, null);
	}

	public ScheduleMySQL(Class<T> entryClass,  Funnel<T> funnel, long expectedEntries, double fpp,String bloomPath) {
		super(entryClass, funnel, expectedEntries, fpp, bloomPath);
	}
	
	protected void setStorage(){
		this.storage = StorageMySQLSington.getInstance();
	}

}
