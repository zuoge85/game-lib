package com.isnowfox.web.config;

public class IllegalConfigException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 399614448616522112L;

	public IllegalConfigException() {
	}

	public IllegalConfigException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalConfigException(String message) {
		super(message);
	}

	public IllegalConfigException(Throwable cause) {
		super(cause);
	}
}
