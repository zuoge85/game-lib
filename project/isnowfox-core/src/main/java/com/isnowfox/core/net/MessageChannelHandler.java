package com.isnowfox.core.net;

import com.isnowfox.core.net.message.Message;
import io.netty.channel.ChannelHandlerContext;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageChannelHandler extends ChannelHandler<NetMessageHandler<?>, Message> {
    private NetMessageHandler<?> messageHandler;

    public MessageChannelHandler(NetMessageHandler<?> messageHandler) {
        super(messageHandler);
        this.messageHandler = messageHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("收到消息!{}", msg);
        }
        Session session = ctx.channel().attr(SESSION).get();
        msg.setSession(session);
        messageHandler.onMessage(msg);
    }

}
