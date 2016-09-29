package com.isnowfox.core.net.message.coder;

import com.isnowfox.core.net.ChannelHandler;
import com.isnowfox.core.net.NetMessageHandler;
import com.isnowfox.core.net.Session;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.isnowfox.core.io.Input;
import com.isnowfox.core.io.MarkCompressInput;
import com.isnowfox.core.net.message.Message;
import com.isnowfox.core.net.message.MessageFactory;
import com.isnowfox.core.net.message.MessageProtocol;

/**
 * 
 * @author zuoge85
 *
 */
public class MessageDecoder extends ByteToMessageDecoder {
	private final MessageFactory messageFactory;
	private final NetMessageHandler<?> handler;
	public MessageDecoder(MessageFactory messageFactory, NetMessageHandler<?> handler) {
		if (messageFactory == null) {
			throw new NullPointerException("messageFactory");
		}
		this.messageFactory = messageFactory;
		this.handler = handler;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		Session session = ctx.channel().attr(ChannelHandler.SESSION).get();
		if(handler.onIn(session, in))
		{
			int readerIndex = in.readerIndex();
			int readableLen = in.readableBytes() ;
			if(readableLen>= MessageProtocol.HEAD_LENGTH){
				int len = in.readUnsignedMedium();
				//byte type  = in.readByte();
				in.readByte();
				if(readableLen >= (len + MessageProtocol.HEAD_LENGTH)){
//					ByteBufInputStream bin = new ByteBufInputStream(in);
					Input i = MarkCompressInput.create(in);
					int t = i.readInt();
					int id = i.readInt();
					Message msg = messageFactory.getMessage(t, id);
					msg.decode(i);
					out.add(msg);
					return;
				}
			}
			in.readerIndex(readerIndex);
		}
	}
}
