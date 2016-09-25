package com.isnowfox.web;

import com.isnowfox.web.codec.Uri;


public interface Dispatcher {

//	public abstract void service(ChannelHandlerContext chc, MessageEvent evt,
//			byte[] post) throws Exception;

	//boolean isStatic(Uri uri);

	boolean disposeStaticFile(Uri uri, Response resp) throws Exception;

	void service(Uri uri, Request req, Response resp);

}