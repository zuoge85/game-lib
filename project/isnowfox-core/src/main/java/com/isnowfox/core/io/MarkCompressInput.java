package com.isnowfox.core.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.EmptyByteBuf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 按位压缩
 * 
 * @author zuoge85
 * 
 */
public abstract class MarkCompressInput extends AbstractInput implements MarkCompressProtocol{
	public final static Input create(InputStream in){
		return create(in, DEFAULT_CHARSET, DEFAULT_IS_BIG_ENDIAN);
	}
	
	public final static Input create(InputStream in, boolean isBigEndian){
		return create(in, DEFAULT_CHARSET, isBigEndian);
	}
	
	public final static Input create(InputStream in, String charset){
		return create(in, charset, DEFAULT_IS_BIG_ENDIAN);
	}
	
	public final static Input create(InputStream in, String charset, boolean isBigEndian){
		if(isBigEndian){
			return new BigEndianInput(in, charset);
		}else{
			return new LittleEndianInput(in, charset);
		}
	}

	public final static Input create(ByteBuf in){
		return create(in, DEFAULT_CHARSET, DEFAULT_IS_BIG_ENDIAN);
	}

	public final static Input create(ByteBuf in, boolean isBigEndian){
		return create(in, DEFAULT_CHARSET, isBigEndian);
	}

	public final static Input create(ByteBuf in, String charset){
		return create(in, charset, DEFAULT_IS_BIG_ENDIAN);
	}

	public final static Input create(ByteBuf in, String charset, boolean isBigEndian){
		if(isBigEndian){
			return new BigEndianInput(in, charset);
		}else{
			return new LittleEndianInput(in, charset);
		}
	}

	private ByteBuf byteBuf;
	
	private MarkCompressInput(InputStream in, String charset) {
		super(in, charset);
	}

	private MarkCompressInput(ByteBuf in, String charset) {
		super(new ByteBufInputStream(in), charset);
		this.byteBuf = in;
	}


	public  ByteBuf getByteBuf(){
		return byteBuf;
	}

	/* (non-Javadoc)
	 * @see com.isnowfox.core.io.bytes.Input#close()
	 */
	@Override
	public void close() throws IOException {
		in.close();
	}

	ArrayList<Item> readAll() throws IOException, ProtocolException{
		ArrayList<Item> array  = new ArrayList<Item>();
		
		for (int  b = in.read(); ;b = in.read()) {
			switch (b) {
			case TYPE_NULL_OR_ZERO_OR_FALSE:
				array.add(new Item("TYPE_NULL_OR_ZERO_OR_FALSE", 0));
				break;
			case TYPE_TRUE:
				array.add(new Item("TYPE_TRUE", 1));
				break;
			case TYPE_INT_1BYTE:
				array.add(new Item("TYPE_INT_1BYTE", in.read()));
				break;
			case TYPE_INT_2BYTE:
				array.add(new Item("TYPE_INT_2BYTE", getShort()));
				break;
			case TYPE_INT_3BYTE:
				array.add(new Item("TYPE_INT_3BYTE", getThree()));
				break;
			case TYPE_INT_4BYTE:
				array.add(new Item("TYPE_INT_4BYTE", getInt()));
				break;
			case TYPE_INT_5BYTE:
				array.add(new Item("TYPE_INT_5BYTE", getFive()));
				break;
			case TYPE_INT_6BYTE:
				array.add(new Item("TYPE_INT_6BYTE", getSix()));
				break;
			case TYPE_INT_7BYTE:
				array.add(new Item("TYPE_INT_7BYTE", getSeven()));
				break;
			case TYPE_INT_8BYTE:
				array.add(new Item("TYPE_INT_8BYTE", getLong()));
				break;
			case TYPE_STRING:
			{
				int length = readInt();
				byte[] bs = new byte[length];
				in.read(bs);
				array.add(new Item("TYPE_STRING", new String(bs, charset)));
				break;
			}
			case TYPE_BYTES:
			{
				int length = readInt();
				byte[] bs = new byte[length];
				in.read(bs);
				array.add(new Item("TYPE_BYTES", bs));
				break;
			}
			case -1:
				return array;
			default:
				if(b > TYPE_MIN && b < TYPE_MAX){
					array.add(new Item("TYPE_MIN - TYPE_MAX",  b - TYPE_MINUS));
					break;
				}else{
					throw  ProtocolException.newTypeException(b);
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.isnowfox.core.io.bytes.Input#readBoolean()
	 */
	@Override
	public boolean readBoolean() throws IOException, ProtocolException {
		int b = in.read();
		if(b == TYPE_TRUE){
			return true;
		}else if(b == TYPE_NULL_OR_ZERO_OR_FALSE){
			return false;
		}else{
			throw ProtocolException.newTypeException(b);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.isnowfox.core.io.bytes.Input#readInt()
	 */
	@Override
	public int readInt() throws IOException, ProtocolException {
		int b = in.read();
		switch (b) {
		case TYPE_TRUE:
			return 1;
		case TYPE_NULL_OR_ZERO_OR_FALSE:
			return 0;
		case TYPE_INT_1BYTE:
			return in.read();
		case TYPE_INT_2BYTE:
			return getShort();
		case TYPE_INT_3BYTE:
			return getThree();
		case TYPE_INT_4BYTE:
			return getInt();
		default:
			if(b > TYPE_MIN && b < TYPE_MAX){
				return b - TYPE_MINUS;
			}else{
				throw  ProtocolException.newTypeException(b);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.isnowfox.core.io.bytes.Input#readLong()
	 */
	@Override
	public long readLong() throws IOException, ProtocolException {
		int b = in.read();
		switch (b) {
		case TYPE_TRUE:
			return 1;
		case TYPE_NULL_OR_ZERO_OR_FALSE:
			return 0;
		case TYPE_INT_1BYTE:
			return in.read();
		case TYPE_INT_2BYTE:
			return getShort();
		case TYPE_INT_3BYTE:
			return getThree();
		case TYPE_INT_4BYTE:
			return getInt();
		case TYPE_INT_5BYTE:
			return getFive();
		case TYPE_INT_6BYTE:
			return getSix();
		case TYPE_INT_7BYTE:
			return getSeven();
		case TYPE_INT_8BYTE:
			return getLong();
		default:
			if(b > TYPE_MIN && b < TYPE_MAX){
				return b - TYPE_MINUS;
			}else{
				throw  ProtocolException.newTypeException(b);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.isnowfox.core.io.bytes.Input#readDouble()
	 */
	@Override
	public double readDouble() throws IOException, ProtocolException {
		long l = readLong();
		return Double.longBitsToDouble(l);
	}
	
	@Override
	public float readFloat() throws IOException, ProtocolException {
		int i = readInt();
		return Float.intBitsToFloat(i);
	}
	
	@Override
	public String readString() throws IOException, ProtocolException {
		int b = in.read();
		if(b == TYPE_STRING){
			int length = readInt();
			byte[] bs = new byte[length];
			in.read(bs);
			return new String(bs, charset);
		}else if(b == TYPE_NULL_OR_ZERO_OR_FALSE){
			return null;
		}else{
			throw  ProtocolException.newTypeException(b);
		}
	}
	
	@Override
	public byte[] readBytes() throws IOException, ProtocolException {
		int b = in.read();
		if(b == TYPE_BYTES){
			int length = readInt();
			byte[] bs = new byte[length];
			in.read(bs);
			return bs;
		}else if(b == TYPE_NULL_OR_ZERO_OR_FALSE){
			return null;
		}else{
			throw  ProtocolException.newTypeException(b);
		}
	}

	public ByteBuf readByteBuf() throws IOException, ProtocolException{
		int length = readInt();
		if(length > 0){
			ByteBuf copy = byteBuf.copy(byteBuf.readerIndex(), length);
			byteBuf.readerIndex(byteBuf.readerIndex() + length);
			return copy;
		}else if(length < 0){
			return null;
		}else{
			return byteBuf.alloc().buffer(0);
		}
	}

	protected abstract  int getShort() throws IOException;
	protected abstract  int getThree() throws IOException;
	protected abstract  int getInt() throws IOException;
	
	protected abstract  long getFive() throws IOException;
	protected abstract  long getSix() throws IOException;
	protected abstract  long getSeven() throws IOException;
	protected abstract  long getLong() throws IOException;
	
	private static final class BigEndianInput extends MarkCompressInput {
		public BigEndianInput(InputStream in, String charset) {
			super(in, charset);
		}

		public BigEndianInput(ByteBuf in, String charset) {
			super(in, charset);
		}

		@Override
		protected int getShort() throws IOException {
			int ch1 = in.read();
	        int ch2 = in.read();
	        return ((ch1 << 8) + (ch2 << 0));
		}

		@Override
		protected int getThree() throws IOException {
			int ch1 = in.read();
	        int ch2 = in.read();
	        int ch3 = in.read();
	        return ((ch1 << 16) + (ch2 << 8) + (ch3 << 0));
		}

		@Override
		protected int getInt() throws IOException {
			int ch1 = in.read();
	        int ch2 = in.read();
	        int ch3 = in.read();
	        int ch4 = in.read();
	        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
		}

		@Override
		protected long getFive() throws IOException {
			long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
	        return ((ch1 << 32) + (ch2 << 24) + (ch3 << 16) + (ch4 << 8) + (ch5 << 0));
		}

		@Override
		protected long getSix() throws IOException {
			long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
			long ch6 = in.read();
	        return ((ch1 << 40) + (ch2 << 32) + (ch3 << 24) + (ch4 << 16) + (ch5 << 8) + (ch6 << 0));
		}

		@Override
		protected long getSeven() throws IOException {
			long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
			long ch6 = in.read();
			long ch7 = in.read();
	        return ((ch1 << 48) + (ch2 << 40) + (ch3 << 32) + (ch4 << 24) + (ch5 << 16) + (ch6 << 8) + (ch7 << 0));
		}

		@Override
		protected long getLong() throws IOException {
			long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
			long ch6 = in.read();
			long ch7 = in.read();
			long ch8 = in.read();
	        return ((ch1 << 56) + (ch2 << 48) + (ch3 << 40) + (ch4 << 32) + (ch5 << 24) + (ch6 << 16) + (ch7 << 8) + (ch8 << 0));
		}
	}
	
	private static final class LittleEndianInput extends MarkCompressInput {
		public LittleEndianInput(InputStream in, String charset) {
			super(in, charset);
		}


		public LittleEndianInput(ByteBuf in, String charset) {
			super(in, charset);
		}
		@Override
		protected int getShort() throws IOException {
			int ch1 = in.read();
	        int ch2 = in.read();
	        return ((ch1 << 0) + (ch2 << 8));
		}

		@Override
		protected int getThree() throws IOException {
			int ch1 = in.read();
	        int ch2 = in.read();
	        int ch3 = in.read();
	        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16));
		}

		@Override
		protected int getInt() throws IOException {
			int ch1 = in.read();
	        int ch2 = in.read();
	        int ch3 = in.read();
	        int ch4 = in.read();
	        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
		}

		@Override
		protected long getFive() throws IOException {
			long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
	        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24)+ (ch5 << 32));
		}

		@Override
		protected long getSix() throws IOException {
			long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
			long ch6 = in.read();
	        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24) + (ch5 << 32) + (ch6 << 40));
		}

		@Override
		protected long getSeven() throws IOException {
			long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
			long ch6 = in.read();
			long ch7 = in.read();
	        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24) + (ch5 << 32) + (ch6 << 40) + (ch7 << 48));
		}

		@Override
		protected long getLong() throws IOException {
			long ch1 = in.read();
			long ch2 = in.read();
			long ch3 = in.read();
			long ch4 = in.read();
			long ch5 = in.read();
			long ch6 = in.read();
			long ch7 = in.read();
			long ch8 = in.read();
	        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24) + (ch5 << 32) + (ch6 << 40) + (ch7 << 48) + (ch8 << 56));
		}
	}
	
	static class Item{
		private String type;
		private Object value;
		
		public Item() {
			
		}
		
		public Item(String type, Object value) {
			this.type = type;
			this.value = value;
		}


		public String getType() {
			return type;
		}


		public void setType(String type) {
			this.type = type;
		}


		public Object getValue() {
			return value;
		}


		public void setValue(Object value) {
			this.value = value;
		}


		@Override
		public String toString() {
			return "[type=" + type + ", value=" + value + "]";
		}
	}
}
