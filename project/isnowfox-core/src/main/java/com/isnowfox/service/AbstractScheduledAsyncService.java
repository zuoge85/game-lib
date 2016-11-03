package com.isnowfox.service;

import com.isnowfox.core.thread.ScheduledExecutorsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 带id 的任务提交,
 * 根据id 分配一个现场执行,
 * id 一样,肯定在一个线程执行
 * 异步服务，请注意线程安全
 *
 * @author zuoge85 on 16-10-6.
 */
public abstract class AbstractScheduledAsyncService {
    private static final Logger log = LoggerFactory.getLogger(AbstractScheduledAsyncService.class);

    private final ScheduledThreadPoolExecutor asyncExecutor;
    private final ScheduledThreadPoolExecutor executors[];
    private final Thread executorsThread[];

    /**
     * @param threadNums     执行普通任务的线程数量, 当等于0 的时候关闭 普通任务
     * @param hashThreadNums 执行hash任务的线程数量
     */
    public AbstractScheduledAsyncService(int threadNums, int hashThreadNums) {
        if (threadNums > 0) {
            asyncExecutor = ScheduledExecutorsUtils.newFixedThreadPool(
                    threadNums,
                    (t, e) -> log.error("线程池错误，会恢复！", e),
                    r -> new Thread(r, this.getClass().getName() + ":AsyncTaskService AsyncThread")
            );
        } else {
            asyncExecutor = null;
        }


        executors = new ScheduledThreadPoolExecutor[hashThreadNums];
        executorsThread = new Thread[hashThreadNums];
        for (int i = 0; i < executors.length; i++) {
            int cureIndex = i;
            executors[i] = ScheduledExecutorsUtils.newSingleThreadExecutor(
                    (t, e) -> log.error("线程池错误，会恢复！", e),
                    r -> {
                        Thread thread = new Thread(r, this.getClass().getName() + ":HashAsyncTaskService AsyncThread");
                        executorsThread[cureIndex] = thread;
                        return thread;
                    }
            );
        }
    }

    protected Future<?> submit(Runnable task) {
        return asyncExecutor.submit(task);
    }

    protected ScheduledFuture<?> submit(Runnable task, int delayMilliseconds) {
        return asyncExecutor.scheduleAtFixedRate(task, delayMilliseconds, delayMilliseconds, TimeUnit.MILLISECONDS);
    }

    protected Future<?> submit(int id, Runnable task) {
        return executors[id & (executors.length - 1)].submit(task);
    }

    protected ScheduledFuture<?> submit(int id, Runnable task, int delayMilliseconds) {
        return executors[id & (executors.length - 1)].scheduleAtFixedRate(task, delayMilliseconds, delayMilliseconds, TimeUnit.MILLISECONDS);
    }


    public void checkThread(int id) {
        if (executorsThread[id & (executors.length - 1)] != Thread.currentThread()) {
            throw new RuntimeException("错误的调用,线程检查失败");
        }
    }

    /**
     * TODO 修改等待时间
     *
     * @throws InterruptedException
     */
    @PreDestroy
    public void close() throws InterruptedException {
        asyncExecutor.shutdown();
        log.info("等待executor关闭");
        boolean isCloseSuccess = true;

        for (int i = 0; i < executors.length; i++) {
            executors[i].shutdown();
        }

        isCloseSuccess = asyncExecutor.awaitTermination(10 * 60, TimeUnit.SECONDS);
        for (int i = 0; i < executors.length; i++) {
            isCloseSuccess = isCloseSuccess && executors[i].awaitTermination(10 * 60, TimeUnit.SECONDS);
        }

        log.info("executor关闭，状态:{}", isCloseSuccess);
    }
}
