package com.mingchao.snsspider.http;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public abstract class HttpResource {
	protected String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
	protected int poolSize;
	protected CookieStore cookieStore;
	protected RequestConfig requestConfig;
	protected PoolingHttpClientConnectionManager cm;
	protected CloseableHttpClient httpclient;
	protected Header[] baseHeaders;
	
	public HttpResource(){
		init();
	}
	
	protected abstract void init();

	public Header[] getBaseHeaders() {
		return baseHeaders;
	}

	public void setBaseHeaders(Header[] baseHeaders) {
		this.baseHeaders = baseHeaders;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public PoolingHttpClientConnectionManager getCm() {
		return cm;
	}

	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}
	
	public synchronized void addCookies(final Cookie[] cookies) {
        if (cookies != null) {
            for (final Cookie cooky : cookies) {
                this.addCookie(cooky);
            }
        }
    }

	public synchronized void addCookie(Cookie cookie) {
		cookieStore.addCookie(cookie);
	}

	public synchronized List<Cookie> getCookies() {
		return cookieStore.getCookies();
	}

	public synchronized Cookie getCookie(String key) {
		for (Iterator<Cookie> iterator = cookieStore.getCookies().iterator(); iterator
				.hasNext();) {
			Cookie cookie = iterator.next();
			if (cookie.getName().equals(key)) {
				return cookie;
			}
		}
		return null;
	}
	
	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
