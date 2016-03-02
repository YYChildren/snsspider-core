package com.mingchao.snsspider.http;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;

public class WebDriverWrapper{
	private Log log = LogFactory.getLog(this.getClass());
	private static AtomicInteger idGen = new AtomicInteger(0);
	private final int id;
	private RemoteWebDriver webDriver;
	private CookieSnsStore cookieSnsStore;
	
	public WebDriverWrapper() {
		id = idGen.getAndIncrement();
	}
	
	public int getId() {
		return id;
	}
	
	public RemoteWebDriver getWebDriver() {
		return webDriver;
	}
	public void setWebDriver(RemoteWebDriver webDriver) {
		this.webDriver = webDriver;
	}
	public synchronized CookieSnsStore getCookieStore() {
		return cookieSnsStore;
	}
	public synchronized void setCookieStore(CookieSnsStore cookieSnsStore) {
		this.cookieSnsStore = cookieSnsStore;
	}
	
	public synchronized void applyCookieStore(){
		Options options = webDriver.manage();
		for (Iterator<CookieSns> iterator = cookieSnsStore.iterator(); iterator.hasNext();) {
			CookieSns cookieSns =  iterator.next();
			try {
				options.addCookie(cookieSns);
			} catch (Exception e) {
				log.error(cookieSns);
			}
			
		}
	}
	
	public synchronized void applyCookieStore(CookieSnsStore cookieSnsStore){
		setCookieStore(cookieSnsStore);
		applyCookieStore();
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebDriverWrapper other = (WebDriverWrapper) obj;
		if (id != other.id)
			return false;
		return true;
	}
}