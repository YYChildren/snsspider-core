package com.mingchao.snsspider.schedule;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.mingchao.snsspider.model.IdAble;
import com.mingchao.snsspider.model.QueueStatus;
import com.mingchao.snsspider.storage.db.StorageJdbc;
import com.mingchao.snsspider.storage.db.StorageMySQL;
import com.mingchao.snsspider.util.BloomFilterUtil;

public class ScheduleMySQL<T extends IdAble> extends ScheduleJdbc<T> {
	public ScheduleMySQL(Class<T> entryClass,
			Class<? extends QueueStatus> queueStatusClass, Funnel<T> funnel) {
		super(entryClass, queueStatusClass, funnel,
				BloomFilterUtil.EXPECTEDENTRIES);
	}

	public ScheduleMySQL(Class<T> entryClass,
			Class<? extends QueueStatus> queueStatusClass, Funnel<T> funnel,
			long expectedEntries) {
		super(entryClass, queueStatusClass, funnel, expectedEntries,
				BloomFilterUtil.DEFAULT_FPP);
	}

	public ScheduleMySQL(Class<T> entryClass,
			Class<? extends QueueStatus> queueStatusClass, Funnel<T> funnel,
			long expectedEntries, double fpp) {
		super(entryClass, queueStatusClass, funnel, expectedEntries, fpp, null);
	}

	public ScheduleMySQL(Class<T> entryClass,
			Class<? extends QueueStatus> queueStatusClass, Funnel<T> funnel,
			long expectedEntries, double fpp, String bloomPath) {
		super(entryClass, queueStatusClass, funnel, expectedEntries, fpp,
				bloomPath);
	}

	@Override
	public void setStorage(StorageJdbc storage) {
		Preconditions.checkArgument(storage instanceof StorageMySQL,
				"storage is not instanceof  " + StorageMySQL.class);
		this.storage = storage;
	}
}
