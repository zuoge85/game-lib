package com.isnowfox.el;

public class UnknownKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3841247074901227976L;

	/**
	 * 
	 */
	public UnknownKeyException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnknownKeyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnknownKeyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UnknownKeyException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnknownKeyException(Throwable cause) {
		super(cause);
	}

	
}
