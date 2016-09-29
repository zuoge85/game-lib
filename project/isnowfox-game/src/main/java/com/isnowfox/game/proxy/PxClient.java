package com.isnowfox.game.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

/**
 * Sends a list of continent/city pairs to a {@link PxServer} to get the
 * local times of the specified cities.
 */
public class PxClient {
    private final static Logger log = LoggerFactory.getLogger(PxClient.class);

    private static final int DEFAULT_THREAD_NUMS = 8;
    private static final int RETRY_WAIT = 1000;

    public static final PxClient create(String host, int port,
                                        final PxMsgHandler<?> messageHandler) {
        return create(host, port, messageHandler,
                DEFAULT_THREAD_NUMS);
    }

    public static final PxClient create(String host, int port,
                                        final PxMsgHandler<?> messageHandler, int threandNums) {
        return create(new PxMsgFactory(), host, port, messageHandler, threandNums);
    }

    public static final PxClient create(PxMsgFactory pxMsgFactory, String host, int port,
                                        final PxMsgHandler<?> messageHandler, int threandNums) {
        return new PxClient(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("messageDecoder", new PxMsgDecoder(pxMsgFactory));
                p.addLast("messageEncoder", new PxMsgEncoder());

                p.addLast("handler", new PxMsgChannelHandler(messageHandler));
            }
        }, host, port, threandNums);
    }

    //
    private final String host;
    private final int port;
    private final int threandNums;

    private Channel channel;
    private final ChannelInitializer<SocketChannel> initializer;
    private EventLoopGroup group;

    private PxClient(ChannelInitializer<SocketChannel> initializer,
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
                    channel = f.channel();
                    f.await();
                    Throwable throwable = f.cause();
                    if (throwable == null) {
                        return;
                    }
                    log.error("连接失败！等待" + RETRY_WAIT + "开始重试！{}", throwable.getMessage());
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
        return (InetSocketAddress) channel
                .localAddress();
    }

    public void write(Object msg) {
        log.debug("发送消息!{}", msg);
        channel.write(msg);
    }

    public void writeAndFlush(Object msg) {
//		log.debug("发送消息!{}", msg);
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
