package com.mingchao.snsspider.http.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverFactoryImpl implements WebDriverFactory {
	private Class<? extends RemoteWebDriver> webDriverClass;
	private int scriptTimeout = 4;
	private int implicitlyWait = 4;
	private int pageLoadTimeout =8;
	

	@Override
	public RemoteWebDriver createWebDriver() throws InstantiationException, IllegalAccessException {
		RemoteWebDriver webDriver = null;
		webDriver = webDriverClass.newInstance();
		webDriver.manage().timeouts()
				.setScriptTimeout(scriptTimeout, TimeUnit.SECONDS)
				.implicitlyWait(implicitlyWait, TimeUnit.SECONDS)
				.pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);// 确保页面加载
		return webDriver;
	}


	public Class<? extends RemoteWebDriver> getWebDriverClass() {
		return webDriverClass;
	}


	public void setWebDriverClass(Class<? extends RemoteWebDriver> webDriverClass) {
		this.webDriverClass = webDriverClass;
	}


	public int getScriptTimeout() {
		return scriptTimeout;
	}


	public void setScriptTimeout(int scriptTimeout) {
		this.scriptTimeout = scriptTimeout;
	}


	public int getImplicitlyWait() {
		return implicitlyWait;
	}


	public void setImplicitlyWait(int implicitlyWait) {
		this.implicitlyWait = implicitlyWait;
	}


	public int getPageLoadTimeout() {
		return pageLoadTimeout;
	}


	public void setPageLoadTimeout(int pageLoadTimeout) {
		this.pageLoadTimeout = pageLoadTimeout;
	}
	
}
