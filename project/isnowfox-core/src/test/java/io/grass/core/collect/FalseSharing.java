package io.grass.core.collect;


import java.util.concurrent.atomic.AtomicLong;

import com.isnowfox.util.TimeSpan;

public final class FalseSharing
    implements Runnable
{
    public final static int NUM_THREADS = 4; // change
    public final static long ITERATIONS = 1000L;
    private final int arrayIndex;

    private static PaddedAtomicLong[] longs = new PaddedAtomicLong[NUM_THREADS];
    static
    {
        for (int i = 0; i < longs.length; i++)
        {
            longs[i] = new PaddedAtomicLong();
        }
    }

    public FalseSharing(final int arrayIndex)
    {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception
    {
        TimeSpan ts = new TimeSpan();
        runTest();
        System.out.println("duration = " + ts.end());
    }

    private static void runTest() throws InterruptedException
    {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++)
        {
            threads[i] = new Thread(new FalseSharing(i));
        }

        for (Thread t : threads)
        {
            t.start();
        }

        for (Thread t : threads)
        {
            t.join();
        }
    }

    public void run()
    {
        long i = ITERATIONS + 1;
        while (0 != --i)
        {
            longs[arrayIndex].set(i);
        }
    }

    // 这段代码的来历可以看4楼的回复
    public static long sumPaddingToPreventOptimisation(final int index)
    {
        PaddedAtomicLong v = longs[index];
        return 0;//return v.p1 + v.p2 + v.p3 + v.p4 + v.p5 + v.p6;
    }

    public static class PaddedAtomicLong extends AtomicLong
    {
        //public volatile long p1, p2, p3, p4, p5, p6 = 7L;
    }
}
