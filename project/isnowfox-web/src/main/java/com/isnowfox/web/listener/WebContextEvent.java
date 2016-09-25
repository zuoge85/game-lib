package com.isnowfox.web.listener;

import com.isnowfox.web.Context;


public class WebContextEvent extends java.util.EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5459620844917891446L;
	
	public WebContextEvent(Object source) {
		super(source);
	}

	public Context getContext() {
		return (Context) super.getSource();
	}
}
