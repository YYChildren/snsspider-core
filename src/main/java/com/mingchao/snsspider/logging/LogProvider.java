package com.mingchao.snsspider.logging;

/**
 * Interface to be implemented upon each Log object.
 *
 * $Id: LogProvider.java,v 1.2 2003/03/27 17:44:03 vanrogu Exp $
 *
 * @author G�nther Van Roey
 */
public interface LogProvider {

    /**
     * Creates a Log instance.
     * @return a log instance
     */
    public Log createLog ( String category );

}
