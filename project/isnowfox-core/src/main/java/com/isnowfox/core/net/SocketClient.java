package com.isnowfox.core.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isnowfox.core.net.message.MessageFactory;
import com.isnowfox.core.net.message.coder.MessageDecoder;
import com.isnowfox.core.net.message.coder.MessageEncoder;
import com.isnowfox.core.net.message.coder.PacketDecoder;
import com.isnowfox.core.net.message.coder.PacketEncoder;

/**
 * Sends a list of continent/city pairs to a {@link SocketServer} to get the
 * local times of the specified cities.
 */
public final class SocketClient {
	private final static Logger log = LoggerFactory.getLogger(SocketClient.class);
	
	private static final int DEFAULT_THREAD_NUMS = 8;
	private static final int RETRY_WAIT = 1000;

	public static final SocketClient createMessageClient(
			final MessageFactory messageFactory, String host, int port,
			final NetMessageHandler<?> messageHandler) {
		return createMessageClient(messageFactory, host, port, messageHandler,
				DEFAULT_THREAD_NUMS);
	}

	public static final SocketClient createMessageClient(
			final MessageFactory messageFactory, String host, int port,
			final NetMessageHandler<?> messageHandler, int threandNums) {
		return new SocketClient(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast("messageDecoder", new MessageDecoder(messageFactory, messageHandler));
				p.addLast("messageEncoder", new MessageEncoder());
				
				p.addLast("handler", new MessageChannelHandler(messageHandler));
			}
		}, host, port, threandNums);
	}

	public final static SocketClient createPacketClient(String host, int port,
			final NetPacketHandler<?> handler) {
		return createPacketClient(host, port, handler, DEFAULT_THREAD_NUMS);
	}

	public final static SocketClient createPacketClient(String host, int port,
			final NetPacketHandler<?> handler, int threandNums) {
		return new SocketClient(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast("packetDecoder", new PacketDecoder(handler));
				p.addLast("packetEncoder", new PacketEncoder());
				
				p.addLast("handler", new PacketChannelHandler(handler));
			}
		}, host, port, threandNums);
	}

	//
	private final String host;
	private final int port;
	private final int threandNums;

	private Channel channel;
	// private final MessageFactory messageFactory;
	// private final MessageHandler<?> messageHandler;
	private final ChannelInitializer<SocketChannel> initializer;
	private EventLoopGroup group;

	private SocketClient(ChannelInitializer<SocketChannel> initializer,
			String host, int port, int threandNums) {
		this.host = host;
		this.port = port;
		this.threandNums = threandNums;
		this.initializer = initializer;
	}
	public void connectRetry() throws Exception {
		connect(true);
	}
	public void connectNoRetry() throws Exception {
		connect(false);
	}
	public void connect(boolean retry) throws Exception {
		if (group != null) {
			throw new ConnectException("不能重复连接！");
		}
		group = new NioEventLoopGroup(threandNums);
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(initializer);
			if (retry) {
				while (true) {
					ChannelFuture f = b.connect(host, port);
					f.await();
					Throwable throwable = f.cause();
					if (throwable == null) {
						channel = f.channel();
						return;
					}
					log.error("连接失败！等待" + RETRY_WAIT + "开始重试！{}",throwable.getMessage());
					Thread.sleep(RETRY_WAIT);
				}
			} else {
				ChannelFuture f = b.connect(host, port);
				channel = f.channel();
				f.get();
				// f.channel().closeFuture().sync();
				// log.error("连接失败！");
			}

		} finally {
			// group.shutdownGracefully();
		}
	}

	public InetSocketAddress getLocalAddress() {
		InetSocketAddress inetAddress = (InetSocketAddress) channel
				.localAddress();
		return inetAddress;
	}

	public void write(Object msg) {
		log.debug("发送消息!{}", msg);
		channel.write(msg);
	}
	
	public void writeAndFlush(Object msg) {
		log.debug("发送消息!{}", msg);
		channel.writeAndFlush(msg);
	}

	public Channel getChannel() {
		return channel;
	}

	public void close() throws InterruptedException, ExecutionException {
		group.shutdownGracefully().get();
		group = null;
	}

}
