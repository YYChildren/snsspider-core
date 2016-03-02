package com.mingchao.snsspider.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.cookie.Cookie;

public class CookieSnsStore implements org.apache.http.client.CookieStore, Iterable<CookieSns> {
	private static AtomicInteger idGen = new AtomicInteger(0);
	private final int id;
	private List<CookieSns> cookieSnss;

	public CookieSnsStore(List<CookieSns> cookies) {
		id = idGen.getAndIncrement();
		this.setCookieSnss(cookies);
	}

	public int getId() {
		return id;
	}

	public synchronized List<CookieSns> getCookieSnss() {
		return cookieSnss;
	}

	public synchronized void setCookieSnss(List<CookieSns> cookieSnss) {
		this.cookieSnss = cookieSnss;
	}

	public synchronized void addCookieSns(CookieSns cookie) {
		cookieSnss.add(cookie);
	}
	
	@Override
	public synchronized void addCookie(Cookie cookie) {
		cookieSnss.add(new CookieSns(cookie));
	}

	@Override
	public synchronized List<Cookie> getCookies() {
		return new ArrayList<Cookie>(cookieSnss);
	}

	@Override
	public synchronized boolean clearExpired(Date date) {
		boolean removed = false;
		for (final Iterator<CookieSns> it = cookieSnss.iterator(); it.hasNext();) {
			if (it.next().isExpired(date)) {
				it.remove();
				removed = true;
			}
		}
		return removed;
	}

	@Override
	public synchronized void clear() {
		cookieSnss.clear();
	}

	@Override
	public Iterator<CookieSns> iterator() {
		return cookieSnss.iterator();
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
		CookieSnsStore other = (CookieSnsStore) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
