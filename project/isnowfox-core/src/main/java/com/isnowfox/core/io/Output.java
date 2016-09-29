package com.isnowfox.core.io;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.OutputStream;

/**
 * writeBooleanArray 这类方法必须处理null
 *
 * @author zuoge85
 */
public interface Output {

    public abstract void flush() throws IOException;

    public abstract void close() throws IOException;

    public abstract void writeBoolean(boolean v) throws IOException;

    public abstract void writeInt(int v) throws IOException;

    public abstract void writeLong(long v) throws IOException;

    public abstract void writeFloat(float v) throws IOException;

    public abstract void writeDouble(double v) throws IOException;

    public abstract void writeString(String s) throws IOException;

    public abstract void writeBytes(byte[] bs) throws IOException;

    public abstract void writeByteBuf(ByteBuf byteBuf) throws IOException;

    public abstract void writeBooleanArray(boolean[] bs) throws IOException;

    public abstract void writeIntArray(int[] bs) throws IOException;

    public abstract void writeLongArray(long[] bs) throws IOException;

    public abstract void writeFloatArray(float[] bs) throws IOException;

    public abstract void writeDoubleArray(double[] bs) throws IOException;

    public abstract void writeStringArray(String[] bs) throws IOException;

    public abstract OutputStream getOutputStream();


    /**
     * 可选
     */
    public abstract ByteBuf getByteBuf();
}