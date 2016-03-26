package com.mingchao.snsspider.http.cookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CookieStorePool {
	public static final int DEFALT_SIZE = 1;
	private Map<Integer,CookieSnsStore> cookiesPool;
	private Random random = new Random();
	private int maxSize;
	
	public CookieStorePool(){
		this(DEFALT_SIZE);
	}
	
	public CookieStorePool(int size){
		cookiesPool = new HashMap<Integer,CookieSnsStore>();
		this.maxSize = size;
	}
	
	public synchronized CookieSnsStore getRandomCookies(){
		if(cookiesPool.isEmpty()){
			return null;
		}
		Integer [] keys = cookiesPool.keySet().toArray(new Integer[0]);
		int r = random.nextInt(keys.length);
		return cookiesPool.get(keys[r]);
	}
	
	/**
	 * 
	 * @param cookieList
	 * @return 如果已经还没有满了, 则返回true，否则返回false
	 */
	public synchronized boolean addCookieStore(List<CookieSns> cookieList){
		CookieSnsStore cl = new CookieSnsStore(cookieList);
		return addCookieStore(cl);
	}
	
	/**
	 * 
	 * @param cookies
	 * @return 如果已经还没有满了, 则返回true，否则返回false
	 */
	public synchronized boolean addCookieStore(CookieSnsStore cookies){
		if(!isFull()){
			cookiesPool.put(cookies.getId(), cookies);
		}
		return !isFull();
	}
	
	public synchronized void deleteCookieStore(CookieSnsStore cookies){
		cookiesPool.remove(cookies.getId());
	}
	
	public synchronized boolean isEmpty(){
		return cookiesPool.isEmpty();
	}
	
	public synchronized boolean isFull(){
		return cookiesPool.size() >= maxSize;
	}
	
	public synchronized int size(){
		return cookiesPool.size();
	}
	
	public synchronized void waiting() throws InterruptedException{
		if(isFull()){
			this.wait();
		}
	}
	
	public synchronized void notifying(){
		if(!isFull()){
			this.notify();
		}
	}
	
}
