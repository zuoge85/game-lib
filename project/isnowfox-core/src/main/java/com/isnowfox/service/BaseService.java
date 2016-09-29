package com.isnowfox.service;

import com.isnowfox.core.thread.FrameQueueThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zuoge85 on 2014/12/14.
 */
public class BaseService {
    private static final Logger log = LoggerFactory.getLogger(BaseService.class);

    private static final int RUN_QUEUE_MAX = 8 * 1024;
    private static final int FRAME_TIME_SPAN = 33;
    private FrameQueueThread frameThread;

    public BaseService() {

    }

    public void init() {
        frameThread = new InnerFrameQueueThread("BaseService thread:" + this);
        frameThread.start();
    }


    private void threadMethod(int frameCount, long time, long passedTime) {
        Runnable runnable = new Runnable() {
            public void run() {
            }
        };
        execute(runnable);
    }

    protected final void execute(Runnable runnable) {
        frameThread.add(runnable);
    }

    /**
     * 线程
     */
    private class InnerFrameQueueThread extends FrameQueueThread {

        public InnerFrameQueueThread(String name) {
            super(name, FRAME_TIME_SPAN, RUN_QUEUE_MAX);
        }

        @Override
        protected void frame(int frameCount, long time, long passedTime) {
            super.frame(frameCount, time, passedTime);
            threadMethod(frameCount, time, passedTime);
        }

        @Override
        protected void errorHandler(Throwable e) {
            log.debug("严重异常", e);
        }
    }
}
