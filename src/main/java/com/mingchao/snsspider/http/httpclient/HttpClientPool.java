package com.mingchao.snsspider.http.httpclient;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientPool {
	private String userAgent;
	private int poolSize;
	private CookieStore cookieStore;
	private RequestConfig requestConfig;
	private PoolingHttpClientConnectionManager cm;
	private CloseableHttpClient httpclient;
	private Header[] baseHeaders;
	
	public HttpClientPool(){
	}
	
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
