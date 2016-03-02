package com.mingchao.snsspider.storage.spi;

import java.io.Serializable;
import java.util.List;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;

public class BaseStorageSpi implements StorageSpi{
	
	protected Log log = LogFactory.getLog(this.getClass());

	@Override
	public void insert(Object object) {
	}

	@Override
	public void insert(List<?> list) {
	}

	@Override
	public void insertIgnore(Object object) {
	}

	@Override
	public void insertIgnore(List<?> list) {
	}

	@Override
	public void insertDuplicate(Object object) {
	}

	@Override
	public void insertDuplicate(List<?> list) {
	}

	@Override
	public Object get(Class<?> c, Serializable id) {
		return null;
	}

	@Override
	public List<Object> get(Class<?> c, Serializable idStart, Serializable idEnd) {
		return null;
	}

	@Override
	public Boolean hasMore(Class<?> c, Serializable idStart) {
		return null;
	}

	@Override
	public void delete(Class<?> c, Serializable idStart, Serializable idEnd) {
	}

}
