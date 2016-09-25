package com.isnowfox.game.proxy.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import com.isnowfox.core.net.Session;

public class LogoutPxMsg  implements PxMsg{
	public static final int ID = 3;

	private short sessionId;
	@SuppressWarnings("rawtypes")
	private Session session;
	
	public LogoutPxMsg() {
		
	}
	
	public LogoutPxMsg(short sessionId) {
		this.sessionId = sessionId;
	}

	public short getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(short sessionId) {
		this.sessionId = sessionId;
	}
	
	public LogoutPxMsg(short sessionId, Session<?> session) {
		this.sessionId = sessionId;
		this.session = session;
	}

	@Override
	public void encode(ByteBuf out) throws Exception {
		out.writeShort(sessionId);
	}

	@Override
	public void decode(ByteBuf in, ChannelHandlerContext ctx) throws Exception {
		sessionId = in.readShort();
	}

	@Override
	public int getType() {
		return ID;
	}

	@SuppressWarnings("unchecked")
	public final <T> Session<T> getSession() {
		return session;
	}

	public final <T> void setSession(Session<T> session) {
		this.session = session;
	}

	@Override
	public String toString() {
		return "LogoutPxMsg [sessionId=" + sessionId + ", session=" + session
				+ "]";
	}
}
