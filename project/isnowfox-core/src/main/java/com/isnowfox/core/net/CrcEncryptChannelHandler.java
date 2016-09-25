package com.isnowfox.core.net;

import com.isnowfox.core.net.message.Packet;
import com.isnowfox.core.net.message.coder.CrcEncryptCoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

@SuppressWarnings({"rawtypes","unchecked"})
public class CrcEncryptChannelHandler extends PacketChannelHandler {

	public static final AttributeKey<CrcEncryptCoder> CRC_ENCRYPT = AttributeKey.valueOf("CrcEncryptChannelHandler.CRC_ENCRYPT");

	private final NetPacketHandler<?> messageHandler;
	private final int zipSize;
	private final int encryptValue;
	public CrcEncryptChannelHandler(NetPacketHandler<?> messageHandler, int zipSize, int encryptValue) {
		super(messageHandler);
		this.messageHandler = messageHandler;
		this.zipSize = zipSize;
		this.encryptValue = encryptValue;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		CrcEncryptCoder encryptCoder = new CrcEncryptCoder(zipSize, encryptValue);
		CrcEncryptCoder crcEncryptCoder = ctx.channel().attr(CRC_ENCRYPT).get();
		if(crcEncryptCoder != null){
			throw new Exception("怎么通道已经被绑定了？");
		}
		ctx.channel().attr(CRC_ENCRYPT).set(encryptCoder);
		super.channelActive(ctx);
	}
}
