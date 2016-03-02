package com.mingchao.snsspider.http;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;

public class WebDriverPool {
	public static final int DEFALT_SIZE = 1;
	
	private Queue<WebDriverWrapper> webDrivers;
	// 是否被使用
	private List<WebDriverWrapper> webDriverList;
	private int poolSize;
	private Semaphore semaphore;

	public WebDriverPool() {
		this(DEFALT_SIZE);
	}
	
	public WebDriverPool(int size) {
		this.poolSize = size;
		semaphore = new Semaphore(poolSize);
		
		webDrivers = new LinkedList<WebDriverWrapper>();
		webDriverList = new ArrayList<WebDriverWrapper>();
		for (int i = 0; i < poolSize; i++) {
			WebDriverWrapper webDriverWrapper = newWebDriverWrapper();
			webDrivers.add(webDriverWrapper);
			webDriverList.add(webDriverWrapper);
		}
	}
	
	private WebDriverWrapper newWebDriverWrapper(){
		WebDriverWrapper webDriverWrapper = new WebDriverWrapper();
		replaceWebDriver(webDriverWrapper);
		return webDriverWrapper;
	}

	private void replaceWebDriver(WebDriverWrapper webDriverWrapper){
		RemoteWebDriver oldWebDriver = webDriverWrapper.getWebDriver();
		if(oldWebDriver !=null){
			try {
				oldWebDriver.quit();//关闭旧的webDriver
			} catch (Exception e) {
			}
		}
		RemoteWebDriver webDriver = newWebDriver();
		webDriverWrapper.setWebDriver(webDriver);
	}
	
	private RemoteWebDriver newWebDriver(){
		RemoteWebDriver webDriver = new FirefoxDriver();
		//RemoteWebDriver webDriver = new ChromeDriver();
		ensurePageLoad(webDriver);// 确保页面加载
		return webDriver;
	}

	private void ensurePageLoad(RemoteWebDriver webDriver) {
		// 等待元素出现
		webDriver.manage().timeouts()
			.setScriptTimeout(3, TimeUnit.SECONDS)
			.implicitlyWait(3, TimeUnit.SECONDS)
			//.pageLoadTimeout(5, TimeUnit.SECONDS)
			;
		webDriver.manage().getCookies();
	}

	public synchronized <T> T submit(SubmitTask<T> getTask) {
		try {
			semaphore.acquire();
			WebDriverWrapper webDriverWrapper = webDrivers.poll();
			try {
				T rs = new SubmitTaskWrapper<T>(getTask).submit(webDriverWrapper);
				return rs;
			}finally{
				webDrivers.offer(webDriverWrapper);
			}
		} catch (InterruptedException | RuntimeException e) {
			LogFactory.getLog(getTask.getClass()).warn(e);
			return null;
		}finally{
			semaphore.release();
		}
	}

	public synchronized void quit() {
		for (WebDriverWrapper remoteWebDriver : webDriverList) {
			remoteWebDriver.getWebDriver().quit();
		}
	}
	
	private class SubmitTaskWrapper<T> implements SubmitTask<T>{
		private SubmitTask<T> submitTask;
		private Log log;
		
		public SubmitTaskWrapper(SubmitTask<T> submitTask){
			this.submitTask = submitTask;
			log = LogFactory.getLog(submitTask.getClass());
		}
	
		@Override
		public T submit(WebDriverWrapper webDriverWrapper) {
			try {
				return submitTask.submit(webDriverWrapper);
			} catch (WebDriverException e) {
				log.info(e,e);
				// 如果webDriver崩溃，则以新的webDriver代替
				if(e.getMessage().contains("session deleted")){
					replaceWebDriver(webDriverWrapper);
				}
			}
			return null;
		}
	}
}

