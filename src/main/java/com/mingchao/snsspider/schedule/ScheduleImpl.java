package com.mingchao.snsspider.schedule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;

public abstract class ScheduleImpl<T> implements Schedule<T> {
	
	protected Log log = LogFactory.getLog(this.getClass());
	protected Class<T> entryClass;
	protected BloomFilter<T> filter;
	protected String bloomPath;
	

	@Override
	public boolean containsKey(T e) {
		return false;
	}

	@Override
	public void schadule(List<T> list) {
	}

	@Override
	public void schadule(T e) {
	}

	@Override
	public void reschadule(T e) {
	}

	@Override
	public T fetch() {
		return null;
	}

	@Override
	public void close() {
		dumpFilter();
	}
	
	public void dumpFilter() {
		if(bloomPath == null){
			return;
		}
		OutputStream ops = null;
		try {
			Files.createDirectories(Paths.get(bloomPath));
			ops = new BufferedOutputStream(Files.newOutputStream(Paths.get(getPath())));
			filter.writeTo(ops);
		} catch (IOException e) {
		}finally{
			if (ops != null) {
				try {
					ops.close();
				} catch (IOException e) {
					log.info(e);
				}
			}
		}
	}

	public BloomFilter<T> readFrom(Funnel<T> funnel) {
		if(bloomPath == null){
			return null;
		}
		InputStream in = null;
		try {
			in = new BufferedInputStream(Files.newInputStream(Paths.get(getPath())));
			return BloomFilter.readFrom(in, funnel);
		} catch (IOException e) {
			return null;
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					log.info(e);
				}
			}
		}
	}
	
	private String getPath(){
		return bloomPath + File.pathSeparator + entryClass.getSimpleName();
	}

}
