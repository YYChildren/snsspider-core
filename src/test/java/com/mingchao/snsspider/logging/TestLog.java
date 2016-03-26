package com.mingchao.snsspider.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestLog {
	public static void main(String[] args) {
		Log log = LogFactory.getLog(TestLog.class);
		log.trace("1");
		log.debug("2");
		log.info("3");
		log.warn("4");
		log.error("5");
		log.fatal("6");
	}
}
