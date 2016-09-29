package io.grass.core.collect;

import com.lmax.disruptor.*;
import com.lmax.disruptor.util.PaddedLong;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lmax.disruptor.RingBuffer.createSingleProducer;

/**
 * @author zuoge85
 */
public class DisruptorTest {
    private static final int NUM_EVENT_PROCESSORS = 3;
    private static final int BUFFER_SIZE = 1024 * 8;
    private static final long ITERATIONS = 1000L * 1000L * 100L;

    private final long[] results = new long[NUM_EVENT_PROCESSORS];

    {
        for (long i = 0; i < ITERATIONS; i++) {
            results[0] = Operation.ADDITION.op(results[0], i);
            results[1] = Operation.SUBTRACTION.op(results[1], i);
            results[2] = Operation.AND.op(results[2], i);
        }
    }

    private final RingBuffer<ValueEvent> ringBuffer = createSingleProducer(
            ValueEvent.EVENT_FACTORY, BUFFER_SIZE, new YieldingWaitStrategy());


    private final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
    private final ValueMutationEventHandler[] handlers = new ValueMutationEventHandler[NUM_EVENT_PROCESSORS];

    {
        handlers[0] = new ValueMutationEventHandler(Operation.ADDITION);
        handlers[1] = new ValueMutationEventHandler(Operation.SUBTRACTION);
        handlers[2] = new ValueMutationEventHandler(Operation.AND);
    }

    private final BatchEventProcessor<?>[] batchEventProcessors = new BatchEventProcessor[NUM_EVENT_PROCESSORS];

    {
        batchEventProcessors[0] = new BatchEventProcessor<ValueEvent>(
                ringBuffer, sequenceBarrier, handlers[0]);
        batchEventProcessors[1] = new BatchEventProcessor<ValueEvent>(
                ringBuffer, sequenceBarrier, handlers[1]);
        batchEventProcessors[2] = new BatchEventProcessor<ValueEvent>(
                ringBuffer, sequenceBarrier, handlers[2]);

        ringBuffer.addGatingSequences(batchEventProcessors[0].getSequence(),
                batchEventProcessors[1].getSequence(),
                batchEventProcessors[2].getSequence());
    }

    public static void main(String[] args) {
        try {
            new DisruptorTest().test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final ExecutorService EXECUTOR = Executors
            .newFixedThreadPool(NUM_EVENT_PROCESSORS);

    public void test() throws InterruptedException {
        //System.out.println(Runtime.getRuntime().availableProcessors());
        CountDownLatch latch = new CountDownLatch(NUM_EVENT_PROCESSORS);
        for (int i = 0; i < NUM_EVENT_PROCESSORS; i++) {
            //System.out.println(batchEventProcessors[i].getSequence().get() +"   "+ ITERATIONS);
            handlers[i].reset(latch, batchEventProcessors[i].getSequence().get() + ITERATIONS);
            EXECUTOR.submit(batchEventProcessors[i]);
        }

        long start = System.currentTimeMillis();

        for (long i = 0; i < ITERATIONS; i++) {
            long sequence = ringBuffer.next();
            ringBuffer.get(sequence).setValue(i);
            ringBuffer.publish(sequence);
        }

        latch.await();
        long opsPerSecond = (ITERATIONS * 1000L) / (System.currentTimeMillis() - start);

        for (int i = 0; i < NUM_EVENT_PROCESSORS; i++) {
            batchEventProcessors[i].halt();
            if (results[i] == handlers[i].getValue()) {

            } else {
                System.out.println("error");
            }
        }
        EXECUTOR.shutdown();
        System.out.format("Run %d, Disruptor=%,d ops/sec%n", 1, opsPerSecond);
    }

    public static final class ValueEvent {
        private long value;

        public long getValue() {
            return value;
        }

        public void setValue(final long value) {
            this.value = value;
        }

        public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
            public ValueEvent newInstance() {
                return new ValueEvent();
            }
        };
    }


    public enum Operation {
        ADDITION {
            @Override
            public long op(final long lhs, final long rhs) {
                return lhs + rhs;
            }
        },

        SUBTRACTION {
            @Override
            public long op(final long lhs, final long rhs) {
                return lhs - rhs;
            }
        },

        AND {
            @Override
            public long op(final long lhs, final long rhs) {
                return lhs & rhs;
            }
        };

        public abstract long op(final long lhs, final long rhs);
    }

    public final class ValueMutationEventHandler implements
            EventHandler<ValueEvent> {
        private final Operation operation;
        private final PaddedLong value = new PaddedLong();
        private long count;
        private CountDownLatch latch;

        public ValueMutationEventHandler(final Operation operation) {
            this.operation = operation;
        }

        public long getValue() {
            return value.get();
        }

        public void reset(final CountDownLatch latch, final long expectedCount) {
            value.set(0L);
            this.latch = latch;
            count = expectedCount;
        }

        @Override
        public void onEvent(final ValueEvent event, final long sequence,
                            final boolean endOfBatch) throws Exception {
            value.set(operation.op(value.get(), event.getValue()));

            if (count == sequence) {
                latch.countDown();
            }
        }
    }
}
