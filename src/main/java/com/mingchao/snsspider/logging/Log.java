package com.mingchao.snsspider.logging;

/**
 * $Id: Log.java,v 1.2 2003/03/27 17:44:03 vanrogu Exp $
 *
 * @author  G�nther Van Roey
 */
public interface Log {

  boolean isDebugEnabled();

  boolean isErrorEnabled();

  boolean isFatalEnabled();

  boolean isInfoEnabled();

  boolean isTraceEnabled();

  boolean isWarnEnabled();

  void trace(Object object);

  void trace(Object object, Throwable throwable);

  void debug(Object object);

  void debug(Object object, Throwable throwable);

  void info(Object object);

  void info(Object object, Throwable throwable);

  void warn(Object object);

  void warn(Object object, Throwable throwable);

  void error(Object object);

  void error(Object object, Throwable throwable);

  void fatal(Object object);

  void fatal(Object object, Throwable throwable);

}
