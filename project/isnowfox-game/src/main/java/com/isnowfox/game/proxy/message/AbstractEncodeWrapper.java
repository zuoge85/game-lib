package com.isnowfox.game.proxy.message;

import com.isnowfox.core.io.MarkCompressOutput;
import com.isnowfox.core.io.Output;
import com.isnowfox.core.net.Session;
import com.isnowfox.core.net.message.Message;
import com.isnowfox.core.net.message.MessageException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * ProxyMessage
 * <p>
 * 消息格式 头[_ _ _ _]类型[_]buf[_________________]扩展内容[____________]
 * 服务器之间转发的消息
 *
 * @author zuoge85
 */
public abstract class AbstractEncodeWrapper implements PxMsg {
    private static final int HEAD_LENGTH = 4;

    @SuppressWarnings("rawtypes")
    private Session session;
    protected Message msg;

    @Override
    public abstract int getType();

    @Override
    public void encode(ByteBuf out) throws Exception {
        int startIdx = out.writerIndex();
        //MessageProtocol.LENGTH_BYTE_NUMS 修改后必须修改这个代码
        out.writeInt(0);

//    	ByteBufOutputStream bout = new ByteBufOutputStream(out);
        Output o = MarkCompressOutput.create(out);
        o.writeInt(msg.getMessageType());
        o.writeInt(msg.getMessageId());
        msg.encode(o);

        int endIdx = out.writerIndex();
        int len = endIdx - startIdx - HEAD_LENGTH;
        out.setInt(startIdx, len);

        encodeData(out);
    }

    protected abstract void encodeData(ByteBuf out) throws Exception;


    @Override
    public void decode(ByteBuf in, ChannelHandlerContext ctx) throws Exception {
//		bufLen = in.readInt();
//		buf = ctx.alloc().buffer(bufLen);
//		bufOffset = 0;
//		decodeData(in);
        throw new MessageException("包装不实现解码！");
    }

//	protected abstract void decodeData(ByteBuf out) throws Exception;

    @SuppressWarnings("unchecked")
    public final <T> Session<T> getSession() {
        return session;
    }

    public final <T> void setSession(Session<T> session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "AbstractWrapper [session=" + session + ", msg=" + msg + "]";
    }
}
