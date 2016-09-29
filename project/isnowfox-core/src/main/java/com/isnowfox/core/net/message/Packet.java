package com.isnowfox.core.net.message;

import com.isnowfox.core.net.Session;
import com.isnowfox.util.ArrayExpandUtils;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;

public final class Packet implements ReferenceCounted {
    private int length;
    private byte type;

    private ByteBuf buf;
    protected int bufOffset;

    @SuppressWarnings("rawtypes")
    private Session session;


    public Packet() {

    }

    public Packet(int length, byte type, ByteBuf buf) {
        this.length = length;
        this.type = type;
        this.buf = buf;
    }

    public Packet(int length, byte type, ByteBuf buf, int bufOffset) {
        this.length = length;
        this.type = type;
        this.buf = buf;
        this.bufOffset = bufOffset;
    }

    /**
     * 不包括消息头
     */
    public final int getLength() {
        return length;
    }

    public final void setLength(int length) {
        this.length = length;
    }

    public final byte getType() {
        return type;
    }

    public final void setType(byte type) {
        this.type = type;
    }

    public final ByteBuf getBuf() {
        return buf;
    }

    public final void setBuf(ByteBuf buf) {
        this.buf = buf;
    }

    @SuppressWarnings("unchecked")
    public final <T> Session<T> getSession() {
        return session;
    }

    public final <T> void setSession(Session<T> session) {
        this.session = session;
    }

    public final int getBufOffset() {
        return bufOffset;
    }

    public final void setBufOffset(int bufOffset) {
        this.bufOffset = bufOffset;
    }

    @Override
    public String toString() {
        if (buf.hasArray()) {
            return "Packet [length=" + length + ", type=" + type + ", buf=" + ArrayExpandUtils.toString(buf.array(), bufOffset, 16)
                    + ", bufOffset=" + bufOffset + ", session=" + session + "]";
        } else {
            int len = Math.min(length, 16);
            byte[] array = new byte[len];
            buf.getBytes(bufOffset, array, 0, len);
            return "Packet [length=" + length + ", type=" + type + ", buf=" + ArrayExpandUtils.toString(array)
                    + ", bufOffset=" + bufOffset + ", session=" + session + "]";
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
    public ReferenceCounted touch() {
        buf.touch();
        return this;
    }

    @Override
    public ReferenceCounted touch(Object hint) {
        buf.touch(hint);
        return this;
    }

    @Override
    public boolean release() {
        return buf.release();
    }

    @Override
    public boolean release(int decrement) {
        return buf.release(decrement);
    }
}
