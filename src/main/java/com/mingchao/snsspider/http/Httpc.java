package com.mingchao.snsspider.http;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public abstract  class Httpc {
	protected String serdir = "D:\\QcUsers\\Win7\\Administrator\\jeclipse_workspace\\spider\\snsspider\\store";
	protected String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
	protected int poolSize = 8;
	protected CookieStore cookieStore;
	protected RequestConfig requestConfig;
	protected PoolingHttpClientConnectionManager cm;
	protected CloseableHttpClient httpclient;
	protected Header[] baseHeaders;
	
	public Httpc(){
	}
	
	protected abstract void initHttpClient();
	
	protected abstract void initBaseHeader();
	
	protected abstract String getCookieFile();
	
	protected CookieStore tryGetCookieStoreFromFile() {
		prepareCookieDir();
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(
					Files.newInputStream(Paths.get(serdir + File.separator + getCookieFile())));
			return (CookieStore) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			return null;
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void syncCookieStore() {
		prepareCookieDir();
		ObjectOutputStream ois = null;
		try {
			ois = new ObjectOutputStream(Files.newOutputStream(Paths
					.get(serdir + File.separator + getCookieFile())));
			ois.writeObject(cookieStore);
		} catch (IOException e) {
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
		}
	}
	
	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(BasicCookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}

	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public Cookie getCookie(String key) {
		for (Iterator<Cookie> iterator = cookieStore.getCookies().iterator(); iterator
				.hasNext();) {
			Cookie cookie = iterator.next();
			if (cookie.getName().equals(key)) {
				return cookie;
			}
		}
		return null;
	}
	

	public void prepareCookieDir() {
		File dir = new File(serdir);
		dir.mkdirs();
	}
	
	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Header[] getBaseHeaders() {
		return baseHeaders;
	}

	public void setBaseHeaders(Header[] baseHeaders) {
		this.baseHeaders = baseHeaders;
	}

}
