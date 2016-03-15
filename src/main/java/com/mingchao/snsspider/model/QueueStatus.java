package com.mingchao.snsspider.model;

public interface QueueStatus {
	public String getTableName();

	public void setTableName(String tableName);

	public Long getIdStart();

	public void setIdStart(Long idStart);
	
	public Long getIdEnd();

	public void setIdEnd(Long idEnd);
}
