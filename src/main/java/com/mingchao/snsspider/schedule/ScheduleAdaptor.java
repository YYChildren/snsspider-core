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
import com.mingchao.snsspider.exception.NotImplementedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ScheduleAdaptor<T> implements Schedule<T> {
	
	protected Log log = LogFactory.getLog(this.getClass());
	protected Class<T> entryClass;
	protected BloomFilter<T> filter;
	protected String bloomPath;
	protected boolean closing = false;
	

	@Override
	public boolean containsKey(T e) {
		throw new NotImplementedException();
	}

	@Override
	public void schadule(List<T> list) {
		throw new NotImplementedException();
	}

	@Override
	public void schadule(T e) {
		throw new NotImplementedException();
	}

	@Override
	public void reschadule(T e) {
		throw new NotImplementedException();
	}

	@Override
	public T fetch() {
		throw new NotImplementedException();
	}

	@Override
	public void closing() {
		throw new NotImplementedException();
	}
	
	@Override
	public void dump() {
		dumpFilter();
	}
	
	@Override
	public void close() {
		throw new NotImplementedException();
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
			log.info("Bloom filter write to path: "+ getPath());
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
			BloomFilter<T> filter = BloomFilter.readFrom(in, funnel);
			log.info("Bloom filter read from path: "+ getPath());
			return filter;
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
		return bloomPath + File.separator + entryClass.getSimpleName();
	}
}
