package com.mingchao.snsspider.exception;

public class NPInterruptedException extends RuntimeException {
	private static final long serialVersionUID = 5772773364527668410L;
	private InterruptedException interruptedException;

	public NPInterruptedException(InterruptedException interruptedException) {
		this.interruptedException = interruptedException;
	}

	public InterruptedException getInterruptedException() {
		return interruptedException;
	}

	@Override
	public String toString() {
		return interruptedException.toString();
	}
}
