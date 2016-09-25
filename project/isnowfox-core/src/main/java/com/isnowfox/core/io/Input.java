package com.isnowfox.core.io;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;

/**
 * 需要处理null，实现不能抛出null异常
 * @author zuoge85
 *
 */
public interface Input {

	public abstract void close() throws IOException;

	public abstract boolean readBoolean() throws IOException, ProtocolException;

	public abstract int readInt() throws IOException, ProtocolException;

	public abstract long readLong() throws IOException, ProtocolException;
	
	public abstract float readFloat() throws IOException, ProtocolException;

	public abstract double readDouble() throws IOException, ProtocolException;

	public abstract String readString() throws IOException, ProtocolException;

	public abstract byte[] readBytes() throws IOException, ProtocolException;

	/**
	 * 可选
	 */
	public abstract ByteBuf readByteBuf()  throws IOException, ProtocolException;
	
	public abstract boolean[] readBooleanArray() throws IOException, ProtocolException;
	public abstract int[] readIntArray() throws IOException, ProtocolException;
	public abstract long[] readLongArray() throws IOException, ProtocolException;
	public abstract float[] readFloatArray() throws IOException, ProtocolException;
	public abstract double[] readDoubleArray() throws IOException, ProtocolException;
	public abstract String[] readStringArray() throws IOException, ProtocolException;

	public abstract InputStream getInputStream();

	/**
	 * 可选
	 */
	public abstract ByteBuf getByteBuf();
}