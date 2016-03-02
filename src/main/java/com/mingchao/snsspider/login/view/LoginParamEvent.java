package com.mingchao.snsspider.login.view;

import java.util.EventObject;

public class LoginParamEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5863312194579309770L;
	
	private TintCmd cmd;
	
	public static enum TintCmd{
		CONTINUE,
		CLOSE
	}

	public LoginParamEvent(Object source, TintCmd cmd) {
		super(source);
		this.cmd = cmd;
	}

	public TintCmd getCmd() {
		return cmd;
	}
}