package com.isnowfox.game.proxy;

import com.isnowfox.core.net.ChannelHandler;
import com.isnowfox.core.net.Session;
import com.isnowfox.game.proxy.message.PxMsg;
import io.netty.channel.ChannelHandlerContext;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PxMsgChannelHandler extends ChannelHandler<PxMsgHandler<?>, PxMsg> {
    private PxMsgHandler<?> messageHandler;

    public PxMsgChannelHandler(PxMsgHandler<?> messageHandler) {
        super(messageHandler);
        this.messageHandler = messageHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PxMsg msg)
            throws Exception {
        Session session = ctx.channel().attr(SESSION).get();
        msg.setSession(session);
        messageHandler.onMessage(msg);
    }
}
