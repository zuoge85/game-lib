package com.isnowfox.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface NetHandler<T> {
	void onConnect(Session<T> session) throws Exception;

	void onDisconnect(Session<T> session) throws Exception;

	void onException(Session<T> session, Throwable cause) throws Exception;
	
	Session<T> createSession(ChannelHandlerContext ctx) throws Exception;

	/**
	 *
	 * @param session session
	 * @param in 数据buf
	 * @return 返回true表示可以解析消息，返回false表示不处理！
	 * @throws Exception
	 */
	boolean onIn(Session<T> session, ByteBuf in) throws Exception;
}
