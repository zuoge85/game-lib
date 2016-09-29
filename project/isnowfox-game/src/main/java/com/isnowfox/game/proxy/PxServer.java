package com.isnowfox.game.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

public final class PxServer {
    private static final int DEFAULT_BOSS_THREAD_NUMS = 4;
    private static final int DEFAULT_WORKER_THREAD_NUMS = 8;

    public static final PxServer create(int port, final PxMsgHandler<?> messageHandler) {
        return create(port, messageHandler, DEFAULT_BOSS_THREAD_NUMS, DEFAULT_WORKER_THREAD_NUMS);
    }


    public static final PxServer create(int port,
                                        final PxMsgHandler<?> messageHandler, int bossThreadNums,
                                        int workerThreadNums) {
        return create(new PxMsgFactory(), port, messageHandler, bossThreadNums, workerThreadNums);
    }

    public static final PxServer create(PxMsgFactory pxMsgFactory, int port,
                                        final PxMsgHandler<?> messageHandler, int bossThreadNums,
                                        int workerThreadNums) {
        return new PxServer(port, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("messageDecoder", new PxMsgDecoder(pxMsgFactory));
                p.addLast("messageEncoder", new PxMsgEncoder());

                p.addLast("handler", new PxMsgChannelHandler(messageHandler));
            }
        }, bossThreadNums, workerThreadNums);
    }

    private final int port;
    private final ChannelInitializer<SocketChannel> initializer;
    private final int bossThreadNums;
    private final int workerThreadNums;
    private final static Logger log = LoggerFactory
            .getLogger(PxServer.class);
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private PxServer(int port,
                     ChannelInitializer<SocketChannel> initializer, int bossThreadNums,
                     int workerThreadNums) {
        this.port = port;
        this.initializer = initializer;
        this.bossThreadNums = bossThreadNums;
        this.workerThreadNums = workerThreadNums;
    }

    public void start() throws Exception {
        if (bossGroup != null) {
            throw new ConnectException("不能重复启动监听");
        }
        log.info("启动端口监听！{}", port);
        bossGroup = new NioEventLoopGroup(bossThreadNums);
        workerGroup = new NioEventLoopGroup(workerThreadNums);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(initializer);
            ChannelFuture f = b.bind(port);
            f.get();
        } finally {
            // bossGroup.shutdownGracefully();
            // workerGroup.shutdownGracefully();
        }
    }

    // public void broadcast(Object obj){
    // for (Channel channel : channelList) {
    // channel.write(obj);
    // }
    // }

    public void close() throws InterruptedException, ExecutionException {
        bossGroup.shutdownGracefully().get();
        workerGroup.shutdownGracefully().get();
    }

    public int getPort() {
        return port;
    }
}
