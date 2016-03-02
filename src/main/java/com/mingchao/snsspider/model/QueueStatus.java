package com.mingchao.snsspider.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="t_queue_status")
public class QueueStatus{
	private String tableName;//存储进度的表名
	private Long idStart;
	
	@Id
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getIdStart() {
		return idStart;
	}

	public void setIdStart(Long idStart) {
		this.idStart = idStart;
	}
}
