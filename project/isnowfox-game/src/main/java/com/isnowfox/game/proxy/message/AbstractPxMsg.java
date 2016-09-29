package com.isnowfox.game.proxy.message;

import com.isnowfox.core.net.Session;
import com.isnowfox.util.ArrayExpandUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCounted;

/**
 * ProxyMessage
 * <p/>
 * 消息格式 头[_ _ _ _]类型[_]buf[_________________]扩展内容[____________]
 * 服务器之间转发的消息
 *
 * @author zuoge85
 */
public abstract class AbstractPxMsg implements PxMsg, ReferenceCounted {


    //private byte type;
    //private int userId;

    protected ByteBuf buf;
    protected int bufOffset;
    protected int bufLen;

    @SuppressWarnings("rawtypes")
    private Session session;

    @Override
    public void encode(ByteBuf out) throws Exception {
        out.writeInt(bufLen);
        out.writeBytes(buf, bufOffset, bufLen);
        encodeData(out);
    }

    protected abstract void encodeData(ByteBuf out) throws Exception;


    @Override
    public void decode(ByteBuf in, ChannelHandlerContext ctx) throws Exception {
        bufLen = in.readInt();


//		buf = ctx.alloc().buffer(bufLen);
        buf = ctx.alloc().buffer(bufLen);
        bufOffset = 0;
        in.readBytes(buf, bufLen);
        try {
            decodeData(in);
        } catch (Exception ex) {
            throw ex;
        }
    }

    protected abstract void decodeData(ByteBuf in) throws Exception;


    @Override
    public abstract int getType();


    @SuppressWarnings("unchecked")
    public final <T> Session<T> getSession() {
        return session;
    }

    public final <T> void setSession(Session<T> session) {
        this.session = session;
    }

    public final ByteBuf getBuf() {
        return buf;
    }

    public final int getBufOffset() {
        return bufOffset;
    }

    public final int getBufLen() {
        return bufLen;
    }

    @Override
    public String toString() {
        if (buf.hasArray()) {
            return "PxMsg [buf=" + ArrayExpandUtils.toString(buf.array(), bufOffset, 16) + ", bufOffset=" + bufOffset + ", bufLen="
                    + bufLen + ", session=" + session + "]";
        } else {
            int len = Math.min(bufLen, 16);
            byte[] array = new byte[len];
            buf.getBytes(bufOffset, array, 0, len);
            return "PxMsg [buf=" + ArrayExpandUtils.toString(array, len) + ", bufOffset=" + bufOffset + ", bufLen="
                    + bufLen + ", session=" + session + "]";
        }
    }


    @Override
    public int refCnt() {
        return buf.refCnt();
    }

    @Override
    public ReferenceCounted retain() {
        return buf.retain();
    }

    @Override
    public ReferenceCounted retain(int increment) {
        return buf.retain();
    }

    @Override
    public boolean release() {
        return buf.release();
    }

    @Override
    public boolean release(int decrement) {
        return buf.release(decrement);
    }


    @Override
    public ReferenceCounted touch() {
        buf.touch();
        return this;
    }

    @Override
    public ReferenceCounted touch(Object hint) {
        buf.touch(hint);
        return this;
    }
}
