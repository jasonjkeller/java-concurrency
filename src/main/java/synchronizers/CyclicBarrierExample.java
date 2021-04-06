package synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * A synchronization mechanism that allows a set of threads to all wait for each other to reach a common barrier point
 */
public class CyclicBarrierExample {

    private static final int MAX = 5;
    private static final CyclicBarrier CYCLIC_BARRIER = new CyclicBarrier(MAX, createRunnableBarrierTask());

    public static void main(String[] args) {
        final var executorService = Executors.newFixedThreadPool(9);

        for (int i = 0 ; i < 50 ; i++) {
            executorService.submit(createRunnableTask(i));
        }

        executorService.shutdown();
    }

    public static void doSomeTask(int i) throws InterruptedException, BrokenBarrierException {
        final var threadName = Thread.currentThread().getName();
        CYCLIC_BARRIER.await();
        System.out.println("Hello from " + threadName + " for task " + i);
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

    private static Runnable createRunnableBarrierTask() {
        return () -> {
//            CYCLIC_BARRIER.reset();
            System.out.println(MAX + " threads have started. Time to say hello!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

}
