package com.isnowfox.web;

import java.io.IOException;
import java.io.Writer;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public interface Response  {

	//public abstract void flush() throws IOException;
	
	public abstract void flushAndClose() throws IOException;

	//public abstract void close() throws IOException;
	
	public abstract void setContentType(String contentType);

	public abstract String getContentType();

	Writer getWriter();
	Appendable getAppendable();

	void oneWrite(byte[] data);

	void sendError(int sc);

	void sendError(HttpResponseStatus status);
	void sendError(HttpResponseStatus status,String message);
	void sendError(HttpResponseStatus status,String message,Throwable tw);

	void sendError(int sc, String reasonPhrase);

	void sendRedirect(String location);

    void addHeader(final String name, final Object value);
    
    void setHeader(final String name, final Object value);
    
    void setHeader(final String name, final Iterable<?> values);

    void removeHeader(final String name);
    
    void clearHeaders();
    
    void add(Cookie cookie);
    
    void addCookie(String name, String value);
}