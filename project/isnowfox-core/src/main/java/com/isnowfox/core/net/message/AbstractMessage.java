package com.isnowfox.core.net.message;

import com.isnowfox.core.net.Session;


public abstract class AbstractMessage implements Message {
	@SuppressWarnings("rawtypes")
	private Session session;
	
	@SuppressWarnings("unchecked")
	@Override
	public final <T> Session<T> getSession() {
		return session;
	}

	@Override
	public final <T> void setSession(Session<T> session) {
		this.session = session;
	}
}
