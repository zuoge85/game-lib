package com.isnowfox.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zuoge85 on 14-3-10.
 */
public abstract class FrameThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(FrameThread.class);

    private final int frameTimeSpan;

    private volatile boolean isRun = true;

    public FrameThread(String name, int frameTimeSpan) {
        super(name);
        this.frameTimeSpan = frameTimeSpan;
    }

    @Override
    public void run() {
        threadMethod();
    }

    private void threadMethod() {
        long frameTime = System.currentTimeMillis();
        int frameCount = 0;
        while (isRun) {
            try {
                ++frameCount;
                long time = System.currentTimeMillis();
                long passedTime = time - frameTime;

                frame(frameCount, time, passedTime);
                long span = frameTimeSpan - (System.currentTimeMillis() - time);
                if (span > 0) {
                    Thread.sleep(span);
                }else{
                    log.error("帧延迟:{} --- {}", span, getName());
                }
                frameTime = time;
            } catch (InterruptedException e) {
                break;
            } catch (Throwable e) {
                errorHandler(e);
            }
        }
    }

    protected abstract void frame(int frameCount, long time, long passedTime);


    protected abstract void errorHandler(Throwable e);

    /**
     * 关闭线程，注意，这个回等待当前的frame执行完毕
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        isRun = false;
        interrupt();
        join();
    }
}
