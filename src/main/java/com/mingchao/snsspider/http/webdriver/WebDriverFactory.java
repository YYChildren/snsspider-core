package com.mingchao.snsspider.http.webdriver;

import org.openqa.selenium.remote.RemoteWebDriver;

public interface WebDriverFactory {
	RemoteWebDriver createWebDriver() throws InstantiationException, IllegalAccessException;
}
