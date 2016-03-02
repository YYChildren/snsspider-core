package com.mingchao.snsspider.logging.impl;

import org.apache.commons.logging.LogFactory;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogProvider;

/**
 * $Id: CommonsLoggingLogProvider.java,v 1.1 2003/03/27 17:44:05 vanrogu Exp $
 */
public class CommonsLoggingLogProvider implements LogProvider {

    public Log createLog(String category) {
        return new CommonsLoggingLogImpl(LogFactory.getLog(category));
    }
}
