package com.mingchao.snsspider.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverUtils {
	public static ExpectedCondition<Boolean> pageLoad = new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver drv) {
            return ((JavascriptExecutor) drv).executeScript(
                    "return document.readyState").equals("complete");
        }
    };
    
	public static void waitJSExecute(WebDriver webDriver, long seconds){
        WebDriverWait wait = new WebDriverWait(webDriver, seconds);
        wait.until(pageLoad);
	}
}
