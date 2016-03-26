package com.mingchao.snsspider.exception;

public class StorageException extends RuntimeException {
	private static final long serialVersionUID = 2860915930670024868L;
	private Exception e;
	
	public StorageException(Exception e){
		this.e = e;
	}

	public Exception getE() {
		return e;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName()+ ": " + e.toString();
	}

}
