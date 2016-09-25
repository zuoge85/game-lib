package com.isnowfox.core.net.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.isnowfox.core.io.MarkCompressOutput;
import com.isnowfox.core.io.Output;
import com.isnowfox.core.io.ProtocolException;

public final class MessageUtils {
	
	/**
	 * 只有内容
	 * @param msg
	 * @return
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public static final byte[] toBytes(Message msg) throws IOException, ProtocolException{
		ByteArrayOutputStream ba = new ByteArrayOutputStream(128);
		Output out = MarkCompressOutput.create(ba);
		msg.encode(out);
		return ba.toByteArray();
	}
	
	/**
	 * 包换type 和id 新的字节数组
	 * @param msg
	 * @return
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public static final byte[] toFullBytes(Message msg) throws IOException, ProtocolException{
		ByteArrayOutputStream ba = new ByteArrayOutputStream(128);
		Output out = MarkCompressOutput.create(ba);
		out.writeInt(msg.getMessageType());
		out.writeInt(msg.getMessageId());
		msg.encode(out);
		return ba.toByteArray();
	}
}
