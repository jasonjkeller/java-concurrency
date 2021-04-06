package synchronizers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * A synchronization mechanism that allows one or more threads to wait until
 * a set of operations being performed in other threads completes.
 */
public class CountDownLatchExample {

    private static final int MAX = 5;
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(MAX);

    public static void main(String[] args) {
        final var executorService = Executors.newFixedThreadPool(9);

        for (int i = 0 ; i < 50 ; i++) {
            if (i <= MAX) {
                System.out.println("Counting down: " + (MAX - i));
            }

            if (i == (MAX + 1)) {
                System.out.println("HELLO FROM THREADS!!!!!!!!!");
            }

            executorService.submit(createRunnableTask(i));
            COUNT_DOWN_LATCH.countDown();
        }

        executorService.shutdown();
    }

    public static void doSomeTask(int i) throws InterruptedException {
        final var threadName = Thread.currentThread().getName();
        COUNT_DOWN_LATCH.await();
        System.out.println("Hello from " + threadName + " for task " + i);
    }

    private static Runnable createRunnableTask(int i) {
        return () -> {
            try {
                doSomeTask(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

}
