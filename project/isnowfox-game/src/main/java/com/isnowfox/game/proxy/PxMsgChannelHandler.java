package com.isnowfox.game.proxy;

import io.netty.channel.ChannelHandlerContext;

import com.isnowfox.core.net.ChannelHandler;
import com.isnowfox.core.net.Session;
import com.isnowfox.game.proxy.message.PxMsg;

@SuppressWarnings({"rawtypes","unchecked"})
public class PxMsgChannelHandler extends ChannelHandler<PxMsgHandler<?>,PxMsg> {
	private PxMsgHandler<?> messageHandler;
	public PxMsgChannelHandler(PxMsgHandler<?> messageHandler) {
		super(messageHandler);
		this.messageHandler = messageHandler;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PxMsg msg)
			throws Exception {
//		if(log.isDebugEnabled()){
//			log.debug("收到消息!{}", msg);
//		}
		Session session = ctx.channel().attr(SESSION).get();
		msg.setSession(session);
		messageHandler.onMessage(msg);
	}
}
