package synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * A reusable synchronization barrier, similar in functionality to
 * CyclicBarrier and CountDownLatch but supporting more flexible usage.
 */
public class PhaserExample {

    private static final int MAX = 5;
    private static final Phaser PHASER = new Phaser(MAX);

    public static void main(String[] args) {
        final var executorService = Executors.newFixedThreadPool(9);

        for (int i = 0 ; i < 50 ; i++) {
            executorService.submit(createRunnableTask(i));
        }

        executorService.shutdown();
    }

    public static void doSomeTask(int i) throws InterruptedException, BrokenBarrierException {
        final var threadName = Thread.currentThread().getName();
//        PHASER.arriveAndAwaitAdvance();
        final var arrive = PHASER.arrive();
        System.out.println("Hello from " + threadName + " for task " + i + " during arrival phase " + arrive);
    }

    private static Runnable createRunnableTask(int i) {
        return () -> {
            try {
                doSomeTask(i);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };
    }

}
