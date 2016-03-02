package com.mingchao.snsspider.model;

public abstract class HadoopString {
	public abstract String toHadoopString();

	public String convertNull(Object object) {
		return object == null ? "\\N" : object.toString();
	}
}
