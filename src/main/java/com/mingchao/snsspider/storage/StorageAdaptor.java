package com.mingchao.snsspider.storage;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mingchao.snsspider.exception.NotImplementedException;

public class StorageAdaptor implements Storage{
	
	protected Log log = LogFactory.getLog(this.getClass());

	@Override
	public void insert(Object object) {
		throw new NotImplementedException();
	}

	@Override
	public void insert(List<?> list) {
		throw new NotImplementedException();
	}

	@Override
	public void insertIgnore(Object object) {
		throw new NotImplementedException();
	}

	@Override
	public void insertIgnore(List<?> list) {
		throw new NotImplementedException();
	}

	@Override
	public void insertDuplicate(Object object) {
		throw new NotImplementedException();
	}

	@Override
	public void insertDuplicate(List<?> list) {
		throw new NotImplementedException();
	}

	@Override
	public Object get(Class<?> c, Serializable id) {
		throw new NotImplementedException();
	}

	@Override
	public List<Object> get(Class<?> c, Serializable idStart, Serializable idEnd) {
		throw new NotImplementedException();
	}

	@Override
	public Boolean hasMore(Class<?> c, Serializable idStart) {
		throw new NotImplementedException();
	}

	@Override
	public void delete(Class<?> c, Serializable idStart, Serializable idEnd) {
		throw new NotImplementedException();
	}

	@Override
	public void close() {
		throw new NotImplementedException();
	}

}
