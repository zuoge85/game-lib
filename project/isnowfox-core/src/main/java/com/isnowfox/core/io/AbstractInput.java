package com.isnowfox.core.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractInput implements Input{
	protected final InputStream in;
	protected final String charset;
	
	public AbstractInput(InputStream in, String charset) {
		this.in = in;
		this.charset = charset;
	}

	public InputStream getInputStream(){
		return in;
	}


	@Override public boolean[] readBooleanArray() throws IOException, ProtocolException {
		int len = readInt();
		if(len == -1){
			return null;
		}
		boolean[] a = new boolean[len];
		for (int i = 0; i < len; i++) {
			a[i] = readBoolean();
		}
		return a;
	}

	@Override public int[] readIntArray() throws IOException, ProtocolException {
		int len = readInt();
		if(len == -1){
			return null;
		}
		int[] a = new int[len];
		for (int i = 0; i < len; i++) {
			a[i] = readInt();
		}
		return a;
	}

	@Override public long[] readLongArray() throws IOException, ProtocolException {
		int len = readInt();
		if(len == -1){
			return null;
		}
		long[] a = new long[len];
		for (int i = 0; i < len; i++) {
			a[i] = readLong();
		}
		return a;
	}
	
	@Override public float[] readFloatArray() throws IOException, ProtocolException {
		int len = readInt();
		if(len == -1){
			return null;
		}
		float[] a = new float[len];
		for (int i = 0; i < len; i++) {
			a[i] = readFloat();
		}
		return a;
	}

	@Override public double[] readDoubleArray() throws IOException, ProtocolException {
		int len = readInt();
		if(len == -1){
			return null;
		}
		double[] a = new double[len];
		for (int i = 0; i < len; i++) {
			a[i] = readDouble();
		}
		return a;
	}

	@Override public String[] readStringArray() throws IOException, ProtocolException {
		int len = readInt();
		if(len == -1){
			return null;
		}
		String[] a = new String[len];
		for (int i = 0; i < len; i++) {
			a[i] = readString();
		}
		return a;
	}

}