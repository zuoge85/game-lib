package com.isnowfox.core.io;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractOutput implements Output {

    protected final OutputStream out;
    protected final String charset;

//	public Output(OutputStream out) {
//		this(out, DEFAULT_IS_BIG_ENDIAN, DEFAULT_CHARSET);
//	}

    public AbstractOutput(OutputStream out, String charset) {
        this.out = out;
        this.charset = charset;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    @Override
    public void writeBooleanArray(boolean[] bs) throws IOException {
        if (bs == null) {
            writeInt(-1);
            return;
        }
        writeInt(bs.length);
        for (boolean b : bs) {
            writeBoolean(b);
        }
    }

    @Override
    public void writeIntArray(int[] bs) throws IOException {
        if (bs == null) {
            writeInt(-1);
            return;
        }
        writeInt(bs.length);
        for (int b : bs) {
            writeInt(b);
        }
    }

    @Override
    public void writeLongArray(long[] bs) throws IOException {
        if (bs == null) {
            writeInt(-1);
            return;
        }
        writeInt(bs.length);
        for (long b : bs) {
            writeLong(b);
        }
    }

    @Override
    public void writeFloatArray(float[] bs) throws IOException {
        if (bs == null) {
            writeInt(-1);
            return;
        }
        writeInt(bs.length);
        for (float b : bs) {
            writeFloat(b);
        }
    }

    @Override
    public void writeDoubleArray(double[] bs) throws IOException {
        if (bs == null) {
            writeInt(-1);
            return;
        }
        writeInt(bs.length);
        for (double b : bs) {
            writeDouble(b);
        }
    }

    @Override
    public void writeStringArray(String[] bs) throws IOException {
        if (bs == null) {
            writeInt(-1);
            return;
        }
        writeInt(bs.length);
        for (String b : bs) {
            writeString(b);
        }
    }
}