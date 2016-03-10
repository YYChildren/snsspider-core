package com.mingchao.snsspider.http;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mingchao.snsspider.exception.WebDriverCosingException;
import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.util.Closeable;

public class WebDriverPool implements Closeable {
	public static final int DEFALT_SIZE = 1;

	private Queue<WebDriverWrapper> webDrivers;
	// 是否被使用
	private List<WebDriverWrapper> webDriverList;
	private int poolSize;
	private int semaphore;
	private boolean closing;
	private Log log;

	public WebDriverPool() {
		this(DEFALT_SIZE);
	}

	public WebDriverPool(int size) {
		this.poolSize = size;
		semaphore = poolSize;

		webDrivers = new LinkedList<WebDriverWrapper>();
		webDriverList = new ArrayList<WebDriverWrapper>();
		for (int i = 0; i < poolSize; i++) {
			WebDriverWrapper webDriverWrapper = newWebDriverWrapper();
			webDrivers.add(webDriverWrapper);
			webDriverList.add(webDriverWrapper);
		}
		closing = false;
		log = LogFactory.getLog(this.getClass());
	}

	private WebDriverWrapper newWebDriverWrapper() {
		WebDriverWrapper webDriverWrapper = new WebDriverWrapper();
		replaceWebDriver(webDriverWrapper);
		return webDriverWrapper;
	}

	private void replaceWebDriver(WebDriverWrapper webDriverWrapper) {
		if(closing){
			return;
		}
		RemoteWebDriver oldWebDriver = webDriverWrapper.getWebDriver();
		if (oldWebDriver != null) {
			try {
				oldWebDriver.quit();// 关闭旧的webDriver
			} catch (Exception e) {
			}
		}
		RemoteWebDriver webDriver = newWebDriver();
		webDriverWrapper.setWebDriver(webDriver);
	}

	private RemoteWebDriver newWebDriver() {
		RemoteWebDriver webDriver = new ChromeDriver();
		// RemoteWebDriver webDriver = new PhantomJSDriver();
		ensurePageLoad(webDriver);// 确保页面加载
		return webDriver;
	}

	private void ensurePageLoad(RemoteWebDriver webDriver) {
		// 等待元素出现
		webDriver.manage().timeouts().setScriptTimeout(4, TimeUnit.SECONDS)
				.implicitlyWait(4, TimeUnit.SECONDS)
				.pageLoadTimeout(8, TimeUnit.SECONDS);
		webDriver.manage().getCookies();
	}

	public <T> T submit(SubmitTask<T> getTask) throws InterruptedException {
		if(closing){
			throw new WebDriverCosingException();//正在关闭异常
		}
		WebDriverWrapper webDriverWrapper = null;
		synchronized(this){
			while(semaphore == 0){
				wait();
			}
			semaphore--;
		}
		
		try {
			webDriverWrapper = webDrivers.poll();
			T rs = new SubmitTaskWrapper<T>(getTask)
					.submit(webDriverWrapper);
			return rs;
		} catch (Exception e) {
			throw e;
		}finally{
			if(webDriverWrapper != null){
				webDrivers.offer(webDriverWrapper);
			}
			synchronized(this){
				if(semaphore++ == 0){
					notify();
				}
			}
		}
	}

	@Override
	public void close() {
		closing = true;
		int i =1;
		for (WebDriverWrapper remoteWebDriver : webDriverList) {
			remoteWebDriver.getWebDriver().quit();
			log.warn(this.getClass().getSimpleName()+" Stopped " + i++);
		}
		log.warn(this.getClass().getSimpleName()+" all Stopped");
	}

	private class SubmitTaskWrapper<T> implements SubmitTask<T> {
		private SubmitTask<T> submitTask;
		private Log log;

		public SubmitTaskWrapper(SubmitTask<T> submitTask) {
			this.submitTask = submitTask;
			log = LogFactory.getLog(submitTask.getClass());
		}

		@Override
		public T submit(WebDriverWrapper webDriverWrapper) {
			try {
				return submitTask.submit(webDriverWrapper);
			}catch(WebDriverCosingException e){//DO NOTHING
			}catch (WebDriverException e) {
				log.warn(e, e);
				// 如果webDriver崩溃（不是超时异常），则以新的webDriver代替
				if (!(e instanceof TimeoutException)) {
					replaceWebDriver(webDriverWrapper);
				}
			}
			return null;
		}
	}

}
