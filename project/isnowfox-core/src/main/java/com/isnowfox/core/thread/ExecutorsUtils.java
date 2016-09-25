package com.isnowfox.core.thread;

import java.util.concurrent.*;

public class ExecutorsUtils {

    public final static ErrorHandlerThreadPoolExecutor newSingleThreadExecutor(Thread.UncaughtExceptionHandler errorHandler, ThreadFactory threadFactory) {
        return new ErrorHandlerThreadPoolExecutor(errorHandler, 1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    public static ErrorHandlerThreadPoolExecutor newSingleThreadExecutor(Thread.UncaughtExceptionHandler errorHandler) {
        return new ErrorHandlerThreadPoolExecutor(errorHandler, 1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public static ExecutorService newFixedThreadPool(int nThreads, Thread.UncaughtExceptionHandler errorHandler) {
        return new ErrorHandlerThreadPoolExecutor(errorHandler, nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public static ExecutorService newFixedThreadPool(int nThreads, Thread.UncaughtExceptionHandler errorHandler, ThreadFactory threadFactory) {
        return new ErrorHandlerThreadPoolExecutor(errorHandler, nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }


    private static class ErrorHandlerThreadPoolExecutor extends ThreadPoolExecutor {
        private Thread.UncaughtExceptionHandler errorHandler;

        public ErrorHandlerThreadPoolExecutor(Thread.UncaughtExceptionHandler errorHandler, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                              ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
            this.errorHandler = errorHandler;
        }

        public ErrorHandlerThreadPoolExecutor(Thread.UncaughtExceptionHandler errorHandler, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                              RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
            this.errorHandler = errorHandler;
        }

        public ErrorHandlerThreadPoolExecutor(Thread.UncaughtExceptionHandler errorHandler, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                              ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
            this.errorHandler = errorHandler;
        }

        public ErrorHandlerThreadPoolExecutor(Thread.UncaughtExceptionHandler errorHandler, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            this.errorHandler = errorHandler;
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            if (t == null && r instanceof Future<?>) {
                try {
                    Future<?> future = (Future<?>) r;
                    if (future.isDone()) {
                        future.get();
                    }
                } catch (CancellationException ce) {
                    t = ce;
                } catch (ExecutionException ee) {
                    t = ee.getCause();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            if (t != null) {
                errorHandler.uncaughtException(Thread.currentThread(), t);
            }
        }
    }
}
