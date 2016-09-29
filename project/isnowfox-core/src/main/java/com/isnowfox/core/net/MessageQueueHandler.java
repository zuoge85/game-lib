package com.isnowfox.core.net;

import com.google.common.collect.Queues;
import com.isnowfox.core.net.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

public class MessageQueueHandler<T> implements NetMessageHandler<T> {
    private static final int QUEUE_MAX = 10000;

    private ArrayBlockingQueue<Message> queue = Queues.newArrayBlockingQueue(QUEUE_MAX);

    @Override
    public Session<T> createSession(ChannelHandlerContext ctx) {
        return new Session<>(ctx.channel());
    }

    @Override
    public boolean onIn(Session<T> session, ByteBuf in) throws Exception {
        return true;
    }

    @Override
    public void onConnect(Session<T> session) {

    }

    @Override
    public void onDisconnect(Session<T> session) {

    }

    @Override
    public void onMessage(Message msg) throws InterruptedException {
        queue.put(msg);
    }

    @Override
    public void onException(Session<T> session, Throwable cause) {

    }

    public Message pollMessage() {
        return queue.poll();
    }

    public void drainMessage(Collection<Message> collect) {
        queue.drainTo(collect);
    }
}
