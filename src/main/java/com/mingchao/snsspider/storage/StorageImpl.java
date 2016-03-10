package com.mingchao.snsspider.storage;
import java.io.Serializable;
import java.util.List;

import com.mingchao.snsspider.storage.spi.StorageSpi;

public class StorageImpl implements Storage {

	StorageSpi sspi;
	
	public StorageImpl(StorageSpi sspi){
		this.sspi = sspi;
	}

	@Override
	public void insert(Object object) {
		sspi.insert(object);
	}

	@Override
	public void insert(List<?> list) {
		sspi.insert(list);
	}

	@Override
	public void insertIgnore(Object object) {
		sspi.insertIgnore(object);
	}

	@Override
	public void insertIgnore(List<?> list) {
		sspi.insertIgnore(list);
	}

	@Override
	public void insertDuplicate(Object object) {
		sspi.insertDuplicate(object);
	}

	@Override
	public void insertDuplicate(List<?> list) {
		sspi.insertDuplicate(list);
	}

	@Override
	public Object get(Class<?> c, Serializable id) {
		return sspi.get(c, id);
	}

	@Override
	public List<Object> get(Class<?> c, Serializable idStart, Serializable idEnd) {
		return sspi.get(c, idStart, idEnd);
	}

	@Override
	public Boolean hasMore(Class<?> c, Serializable idStart) {
		return sspi.hasMore(c,idStart);
	}

	@Override
	public void delete(Class<?> c, Serializable idStart, Serializable idEnd) {
		sspi.delete(c, idStart, idEnd);
	}

	@Override
	public void close() {
		sspi.close();
	}
	
}
