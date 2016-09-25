package com.isnowfox.core.io;

public class ProtocolException  extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1591880161791275378L;

	public ProtocolException() {
		super();
	}

	public ProtocolException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolException(String message) {
		super(message);
	}

	public ProtocolException(Throwable cause) {
		super(cause);
	}

	public static final ProtocolException newTypeException(int b){
		return new ProtocolException("error type :" + b);
	}
}
