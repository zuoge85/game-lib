package com.isnowfox.core.net.message.coder;

import com.isnowfox.core.net.ChannelHandler;
import com.isnowfox.core.net.NetPacketHandler;
import com.isnowfox.core.net.Session;
import com.isnowfox.core.net.message.MessageProtocol;
import com.isnowfox.core.net.message.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author zuoge85
 */
public class PacketDecoder extends ByteToMessageDecoder {
    //private final MessageFactory messageFactory;
    private final NetPacketHandler<?> handler;
    public PacketDecoder(NetPacketHandler<?> handler) {
        this.handler = handler;
//		if (messageFactory == null) {
//			throw new NullPointerException("messageFactory");
//		}
//		this.messageFactory = messageFactory;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {
        try {
            Session session = ctx.channel().attr(ChannelHandler.SESSION).get();
            if(handler.onIn(session, in))
            {
                int readerIndex = in.readerIndex();
                int readableLen = in.readableBytes();

                if (readableLen >= MessageProtocol.HEAD_LENGTH) {
                    int len = in.readUnsignedMedium();
                    byte type = in.readByte();
                    if (readableLen >= (len + MessageProtocol.HEAD_LENGTH)) {
                        ByteBuf buf = ctx.alloc().buffer(len);
                        in.readBytes(buf, len);

                        Packet p = new Packet(len, type, buf, 0);
                        out.add(p);
                        return;
                    }
                }
                in.readerIndex(readerIndex);
            }
        } catch (Throwable t) {
            ctx.fireExceptionCaught(t);
        }
    }
}
