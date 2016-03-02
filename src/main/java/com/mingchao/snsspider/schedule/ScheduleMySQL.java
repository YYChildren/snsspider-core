package com.mingchao.snsspider.schedule;

import com.mingchao.snsspider.model.ToBytes;
import com.mingchao.snsspider.storage.StorageMySQLSington;

public class ScheduleMySQL <T extends ToBytes> extends ScheduleJdbc<T>{
	public ScheduleMySQL(Class<T> entityClass){
		super(entityClass);
	}
	
	public ScheduleMySQL(Class<T> entryClass,long expectedEntries) {
		super(entryClass, expectedEntries);
	}

	protected void setStorage(){
		this.storage = StorageMySQLSington.getInstance();
	}

}
