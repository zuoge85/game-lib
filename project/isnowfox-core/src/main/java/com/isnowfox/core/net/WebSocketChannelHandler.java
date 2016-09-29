package com.isnowfox.core.net;

import com.isnowfox.core.net.message.MessageProtocol;
import com.isnowfox.core.net.message.Packet;
import com.isnowfox.core.net.message.coder.CrcEncryptCoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WebSocketChannelHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    protected final static Logger log = LoggerFactory.getLogger(WebSocketChannelHandler.class);

    private static final AttributeKey<Session> SESSION = AttributeKey.valueOf("RmiClientHandler.SESSION");

    private final NetPacketHandler<?> messageHandler;

    public WebSocketChannelHandler(NetPacketHandler<?> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (log.isDebugEnabled()) {
            log.debug("连接成功{}", channel);
        }
        Session session = messageHandler.createSession(ctx);
        ctx.channel().attr(SESSION).set(session);
        messageHandler.onConnect(session);
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        ByteBuf content = frame.content();
        Session session = ctx.channel().attr(SESSION).get();

        content.retain();
        Packet msg = new Packet(
                content.readableBytes(),
                (byte) MessageProtocol.TYPE_NORMAL, content, 0
        );
        msg.setSession(session);
        messageHandler.onPacket(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("连接断开{}", ctx);
        }
        messageHandler.onDisconnect(ctx.channel().attr(SESSION).get());
        ctx.channel().attr(SESSION).remove();
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        messageHandler.onException(ctx.channel().attr(SESSION).get(), cause);
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
