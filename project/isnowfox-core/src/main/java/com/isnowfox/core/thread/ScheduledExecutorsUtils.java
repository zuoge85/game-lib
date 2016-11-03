package com.isnowfox.core.thread;

import java.util.concurrent.*;

public class ScheduledExecutorsUtils {

    public final static ErrorHandlerThreadPoolExecutor newSingleThreadExecutor(Thread.UncaughtExceptionHandler errorHandler, ThreadFactory threadFactory) {
        return new ErrorHandlerThreadPoolExecutor(errorHandler, 1, threadFactory);
    }

    public static ErrorHandlerThreadPoolExecutor newSingleThreadExecutor(Thread.UncaughtExceptionHandler errorHandler) {
        return new ErrorHandlerThreadPoolExecutor(errorHandler, 1);
    }

    public static ScheduledThreadPoolExecutor newFixedThreadPool(int nThreads, Thread.UncaughtExceptionHandler errorHandler) {
        return new ErrorHandlerThreadPoolExecutor(errorHandler, nThreads);
    }

    public static ScheduledThreadPoolExecutor newFixedThreadPool(int nThreads, Thread.UncaughtExceptionHandler errorHandler, ThreadFactory threadFactory) {
        return new ErrorHandlerThreadPoolExecutor(errorHandler, nThreads, threadFactory);
    }


    private static class ErrorHandlerThreadPoolExecutor extends ScheduledThreadPoolExecutor {
        private Thread.UncaughtExceptionHandler errorHandler;

        public ErrorHandlerThreadPoolExecutor(Thread.UncaughtExceptionHandler errorHandler, int corePoolSize) {
            super(corePoolSize);
            this.errorHandler = errorHandler;
        }

        public ErrorHandlerThreadPoolExecutor(Thread.UncaughtExceptionHandler errorHandler, int corePoolSize, ThreadFactory threadFactory) {
            super(corePoolSize, threadFactory);
            this.errorHandler = errorHandler;
        }

        public ErrorHandlerThreadPoolExecutor(Thread.UncaughtExceptionHandler errorHandler, int corePoolSize, RejectedExecutionHandler handler) {
            super(corePoolSize, handler);
            this.errorHandler = errorHandler;
        }

        public ErrorHandlerThreadPoolExecutor(Thread.UncaughtExceptionHandler errorHandler, int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, threadFactory, handler);
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
