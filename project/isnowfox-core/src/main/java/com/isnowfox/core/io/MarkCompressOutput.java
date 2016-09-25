package com.isnowfox.core.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 按位压缩
 *
 * @author zuoge85
 */
public abstract class MarkCompressOutput extends AbstractOutput implements MarkCompressProtocol {

    public final static Output create(OutputStream out) {
        return create(out, DEFAULT_CHARSET, DEFAULT_IS_BIG_ENDIAN);
    }

    public final static Output create(OutputStream out, boolean isBigEndian) {
        return create(out, DEFAULT_CHARSET, isBigEndian);
    }

    public final static Output create(OutputStream out, String charset) {
        return create(out, charset, DEFAULT_IS_BIG_ENDIAN);
    }

    public final static Output create(OutputStream out, String charset, boolean isBigEndian) {
        if (isBigEndian) {
            return new BigEndianOutput(out, charset);
        } else {
            return new LittleEndianOutput(out, charset);
        }
    }

    public final static Output create(ByteBuf out) {
        return create(out, DEFAULT_CHARSET, DEFAULT_IS_BIG_ENDIAN);
    }

    public final static Output create(ByteBuf out, boolean isBigEndian) {
        return create(out, DEFAULT_CHARSET, isBigEndian);
    }

    public final static Output create(ByteBuf out, String charset) {
        return create(out, charset, DEFAULT_IS_BIG_ENDIAN);
    }

    public final static Output create(ByteBuf out, String charset, boolean isBigEndian) {
        if (isBigEndian) {
            return new BigEndianOutput(out, charset);
        } else {
            return new LittleEndianOutput(out, charset);
        }
    }

//	public Output(OutputStream out) {
//		this(out, DEFAULT_IS_BIG_ENDIAN, DEFAULT_CHARSET);
//	}

    private ByteBuf byteBuf;

    public MarkCompressOutput(OutputStream out, String charset) {
        super(out, charset);
    }

    public MarkCompressOutput(ByteBuf byteBuf, String charset) {
        super(new ByteBufOutputStream(byteBuf), charset);
        this.byteBuf = byteBuf;
    }

    @Override
    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    @Override
    public void writeByteBuf(ByteBuf byteBuf) throws IOException {
        if (byteBuf == null) {
            writeInt(-1);
        } else {
            writeInt(byteBuf.readableBytes());
            this.byteBuf.writeBytes(byteBuf);
            byteBuf.release();
        }
    }

    /* (non-Javadoc)
         * @see com.isnowfox.core.io.bytes.Output#flush()
         */
    @Override
    public final void flush() throws IOException {
        out.flush();
    }

    /* (non-Javadoc)
     * @see com.isnowfox.core.io.bytes.Output#close()
     */
    @Override
    public final void close() throws IOException {
        out.close();
    }

    /* (non-Javadoc)
     * @see com.isnowfox.core.io.bytes.Output#writeBoolean(boolean)
     */
    @Override
    public final void writeBoolean(boolean v) throws IOException {
        out.write(v ? TYPE_TRUE : TYPE_NULL_OR_ZERO_OR_FALSE);
    }


    /* (non-Javadoc)
     * @see com.isnowfox.core.io.bytes.Output#writeInt(int)
     */
    @Override
    public final void writeInt(int v) throws IOException {
        if (v > TYPE_MIN - TYPE_MINUS && v < TYPE_MAX - TYPE_MINUS) {
            out.write(v + TYPE_MINUS);
        } else if (v >>> 8 == 0) {
            out.write(TYPE_INT_1BYTE);
            out.write(v);
        } else if (v >>> 16 == 0) {
            out.write(TYPE_INT_2BYTE);
            putShort(v);
        } else if (v >>> 24 == 0) {
            out.write(TYPE_INT_3BYTE);
            putThree(v);
        } else {
            out.write(TYPE_INT_4BYTE);
            putInt(v);
        }
    }

    public final void writeFloatByInt(int v) throws IOException {
        if (v == 0) {
            out.write(TYPE_NULL_OR_ZERO_OR_FALSE);
        } else if (v >>> 8 == 0) {
            out.write(TYPE_INT_1BYTE);
            out.write(v);
        } else if (v >>> 16 == 0) {
            out.write(TYPE_INT_2BYTE);
            putShort(v);
        } else if (v >>> 24 == 0) {
            out.write(TYPE_INT_3BYTE);
            putThree(v);
        } else {
            out.write(TYPE_INT_4BYTE);
            putInt(v);
        }
    }


    /* (non-Javadoc)
     * @see com.isnowfox.core.io.bytes.Output#writeLong(long)
     */
    @Override
    public final void writeLong(long v) throws IOException {
        if (v > TYPE_MIN - TYPE_MINUS && v < TYPE_MAX - TYPE_MINUS) {
            out.write((int) (v + TYPE_MINUS));
        } else if (v >>> 8 == 0) {
            out.write(TYPE_INT_1BYTE);
            out.write((int) (v & 0xFF));
        } else if (v >>> 16 == 0) {
            out.write(TYPE_INT_2BYTE);
            putShort((int) v);
        } else if (v >>> 24 == 0) {
            out.write(TYPE_INT_3BYTE);
            putThree((int) v);
        } else if (v >>> 32 == 0) {
            out.write(TYPE_INT_4BYTE);
            putInt((int) v);
        } else if (v >>> 40 == 0) {
            out.write(TYPE_INT_5BYTE);
            putFive(v);
        } else if (v >>> 48 == 0) {
            out.write(TYPE_INT_6BYTE);
            putSix(v);
        } else if (v >>> 56 == 0) {
            out.write(TYPE_INT_7BYTE);
            putSeven(v);
        } else {
            out.write(TYPE_INT_8BYTE);
            putLong(v);
        }
    }

    private final void writeDoubleByLong(long v) throws IOException {
        if (v == 0) {
            out.write(TYPE_NULL_OR_ZERO_OR_FALSE);
        } else if (v >>> 8 == 0) {
            out.write(TYPE_INT_1BYTE);
            out.write((int) (v & 0xFF));
        } else if (v >>> 16 == 0) {
            out.write(TYPE_INT_2BYTE);
            putShort((int) v);
        } else if (v >>> 24 == 0) {
            out.write(TYPE_INT_3BYTE);
            putThree((int) v);
        } else if (v >>> 32 == 0) {
            out.write(TYPE_INT_4BYTE);
            putInt((int) v);
        } else if (v >>> 40 == 0) {
            out.write(TYPE_INT_5BYTE);
            putFive(v);
        } else if (v >>> 48 == 0) {
            out.write(TYPE_INT_6BYTE);
            putSix(v);
        } else if (v >>> 56 == 0) {
            out.write(TYPE_INT_7BYTE);
            putSeven(v);
        } else {
            out.write(TYPE_INT_8BYTE);
            putLong(v);
        }
    }

    @Override
    public final void writeFloat(float v) throws IOException {
        writeFloatByInt(Float.floatToIntBits(v));
    }

    /* (non-Javadoc)
     * @see com.isnowfox.core.io.bytes.Output#writeDouble(double)
     */
    @Override
    public final void writeDouble(double v) throws IOException {
        writeDoubleByLong(Double.doubleToLongBits(v));
    }

    /* (non-Javadoc)
     * @see com.isnowfox.core.io.bytes.Output#writeString(java.lang.String)
     */
    @Override
    public final void writeString(String s) throws IOException {
        if (s == null) {
            out.write(TYPE_NULL_OR_ZERO_OR_FALSE);
            return;
        }
        byte[] b = s.getBytes(charset);
        out.write(TYPE_STRING);
        writeInt(b.length);
        out.write(b);
    }

    public void writeBytes(byte[] bs) throws IOException {
        if (bs == null) {
            out.write(TYPE_NULL_OR_ZERO_OR_FALSE);
            return;
        }
        out.write(TYPE_BYTES);
        writeInt(bs.length);
        out.write(bs);
    }

    protected abstract void putShort(int v) throws IOException;

    protected abstract void putThree(int v) throws IOException;

    protected abstract void putInt(int v) throws IOException;

    protected abstract void putFive(long v) throws IOException;

    protected abstract void putSix(long v) throws IOException;

    protected abstract void putSeven(long v) throws IOException;

    protected abstract void putLong(long v) throws IOException;

    private static final class BigEndianOutput extends MarkCompressOutput {
        private BigEndianOutput(OutputStream out, String charset) {
            super(out, charset);
        }

        public BigEndianOutput(ByteBuf byteBuf, String charset) {
            super(byteBuf, charset);
        }

        @Override
        protected void putShort(int v) throws IOException {
            out.write((v >>> 8));
            out.write((v >>> 0));
        }

        @Override
        protected void putThree(int v) throws IOException {
            out.write((v >>> 16));
            out.write((v >>> 8));
            out.write((v >>> 0));
        }

        @Override
        protected void putInt(int v) throws IOException {
            out.write((v >>> 24));
            out.write((v >>> 16));
            out.write((v >>> 8));
            out.write((v >>> 0));
        }

        @Override
        protected void putFive(long v) throws IOException {
            out.write((int) ((v >>> 32)));
            out.write((int) ((v >>> 24)));
            out.write((int) ((v >>> 16)));
            out.write((int) ((v >>> 8)));
            out.write((int) ((v >>> 0)));
        }

        @Override
        protected void putSix(long v) throws IOException {
            out.write((int) ((v >>> 40)));
            out.write((int) ((v >>> 32)));
            out.write((int) ((v >>> 24)));
            out.write((int) ((v >>> 16)));
            out.write((int) ((v >>> 8)));
            out.write((int) ((v >>> 0)));
        }

        @Override
        protected void putSeven(long v) throws IOException {
            out.write((int) ((v >>> 48)));
            out.write((int) ((v >>> 40)));
            out.write((int) ((v >>> 32)));
            out.write((int) ((v >>> 24)));
            out.write((int) ((v >>> 16)));
            out.write((int) ((v >>> 8)));
            out.write((int) ((v >>> 0)));
        }

        @Override
        protected void putLong(long v) throws IOException {
            out.write((int) ((v >>> 56)));
            out.write((int) ((v >>> 48)));
            out.write((int) ((v >>> 40)));
            out.write((int) ((v >>> 32)));
            out.write((int) ((v >>> 24)));
            out.write((int) ((v >>> 16)));
            out.write((int) ((v >>> 8)));
            out.write((int) ((v >>> 0)));
        }

    }

    private static final class LittleEndianOutput extends MarkCompressOutput {
        private LittleEndianOutput(OutputStream out, String charset) {
            super(out, charset);
        }

        public LittleEndianOutput(ByteBuf byteBuf, String charset) {
            super(byteBuf, charset);
        }

        @Override
        protected void putShort(int v) throws IOException {
            out.write((v >>> 0));
            out.write((v >>> 8));
        }

        @Override
        protected void putThree(int v) throws IOException {
            out.write((v >>> 0));
            out.write((v >>> 8));
            out.write((v >>> 16));
        }

        protected final void putInt(int v) throws IOException {
            out.write((v >>> 0));
            out.write((v >>> 8));
            out.write((v >>> 16));
            out.write((v >>> 24));
        }

        @Override
        protected void putFive(long v) throws IOException {
            out.write((int) ((v >>> 0)));
            out.write((int) ((v >>> 8)));
            out.write((int) ((v >>> 16)));
            out.write((int) ((v >>> 24)));
            out.write((int) ((v >>> 32)));
        }

        @Override
        protected void putSix(long v) throws IOException {
            out.write((int) ((v >>> 0)));
            out.write((int) ((v >>> 8)));
            out.write((int) ((v >>> 16)));
            out.write((int) ((v >>> 24)));
            out.write((int) ((v >>> 32)));
            out.write((int) ((v >>> 40)));
        }

        @Override
        protected void putSeven(long v) throws IOException {
            out.write((int) ((v >>> 0)));
            out.write((int) ((v >>> 8)));
            out.write((int) ((v >>> 16)));
            out.write((int) ((v >>> 24)));
            out.write((int) ((v >>> 32)));
            out.write((int) ((v >>> 40)));
            out.write((int) ((v >>> 48)));
        }

        @Override
        protected void putLong(long v) throws IOException {
            out.write((int) ((v >>> 0)));
            out.write((int) ((v >>> 8)));
            out.write((int) ((v >>> 16)));
            out.write((int) ((v >>> 24)));
            out.write((int) ((v >>> 32)));
            out.write((int) ((v >>> 40)));
            out.write((int) ((v >>> 48)));
            out.write((int) ((v >>> 56)));
        }
    }
}
