package com.isnowfox.core.thread;

import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

/**
 * 房间服务
 *
 * @author zuoge85@gmail.com on 16/9/30.
 */
@Service
public abstract class FrameQueueContainer {
    protected FrameQueueThread frameThread;

    public FrameQueueContainer(int frameTimeSpan, int queueMax) {
        frameThread = new InnerFrameQueueThread(this.getClass().getName(), frameTimeSpan, queueMax);
    }

    public void start() {
        frameThread.start();
    }


    protected void run(Runnable runnable) {
        frameThread.add(runnable);
    }

    protected abstract void threadMethod(int frameCount, long time, long passedTime);

    protected abstract void errorHandler(Throwable e);

    @PreDestroy
    public void close() throws InterruptedException {
        frameThread.close();
    }

    /**
     * 线程
     */
    private class InnerFrameQueueThread extends FrameQueueThread {
        InnerFrameQueueThread(String name, int frameTimeSpan, int queueMax) {
            super(name, frameTimeSpan, queueMax);
        }

        @Override
        protected void frame(int frameCount, long time, long passedTime) {
            super.frame(frameCount, time, passedTime);
            threadMethod(frameCount, time, passedTime);
        }

        @Override
        protected void errorHandler(Throwable e) {
            FrameQueueContainer.this.errorHandler(e);
        }
    }
}
