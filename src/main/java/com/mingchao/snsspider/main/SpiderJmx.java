package com.mingchao.snsspider.main;

import com.mingchao.snsspider.util.Closeable;

public class SpiderJmx implements SpiderJmxMBean {
	private Closeable closeable;

	public SpiderJmx() {
	}

	public SpiderJmx(Closeable closeable) {
		this.closeable = closeable;
	}

	@Override
	public void close() {
		closeable.close();
	}
}