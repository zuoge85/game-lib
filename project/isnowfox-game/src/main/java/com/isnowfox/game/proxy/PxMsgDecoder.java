package com.isnowfox.game.proxy;

import com.isnowfox.game.proxy.message.PxMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author zuoge85
 */
public class PxMsgDecoder extends ByteToMessageDecoder {
    private final PxMsgFactory messageFactory;

    public PxMsgDecoder(PxMsgFactory messageFactory) {
        if (messageFactory == null) {
            throw new NullPointerException("messageFactory");
        }
        this.messageFactory = messageFactory;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {
        try {
            int readerIndex = in.readerIndex();
            int readableLen = in.readableBytes();
            if (readableLen >= PxMsg.HEAD_LENGTH) {
                int len = in.readInt();
                byte id = in.readByte();
                if (readableLen >= (len + PxMsg.HEAD_LENGTH)) {
                    //解码


//                    ByteBuf buf = Unpooled.buffer(len);
//                    in.readBytes(buf, len);

                    PxMsg pm = messageFactory.get(id);
                    pm.decode(in, ctx);
                    out.add(pm);
                    return;
                }
            }
            in.readerIndex(readerIndex);
        } catch (Throwable t) {
            ctx.fireExceptionCaught(t);
        }
    }
}
