package com.isnowfox.core.thread;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author zuoge85 on 14-3-10.
 */
public abstract class FrameQueueThread extends FrameThread {
    private final ArrayBlockingQueue<Runnable> runQueue;// = new ArrayBlockingQueue<>(RUN_QUEUE_MAX);

    private final ArrayList<Runnable> swapList = new ArrayList<>();

    public FrameQueueThread(String name, int frameTimeSpan, int queueMax) {
        super(name, frameTimeSpan);
        runQueue = new ArrayBlockingQueue<>(queueMax);
    }

    @Override
    protected void frame(int frameCount, long time, long passedTime) {
        threadRunQueue();
    }

    private void threadRunQueue() {
        final ArrayList<Runnable> swapList = this.swapList;
        swapList.clear();
        final int len = runQueue.drainTo(swapList);
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                try {
                    swapList.get(i).run();
                } catch (Throwable th) {
                    errorHandler(th);
                }
            }
        }
    }

    public final boolean add(Runnable run) {
        return runQueue.add(run);
    }


    @Override
    public void close() throws InterruptedException {
        super.close();
        threadRunQueue();
    }
//    /**
//     * 小心线程安全问题，如果在frame里面调用，没有问题
//     * @return
//     */
//    public final ArrayList<Runnable> getSwapList() {
//        return swapList;
//    }
}
