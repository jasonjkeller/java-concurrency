package synchronizers;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphores are used to restrict the number of threads than can execute a task.
 */
public class SemaphoreExample {

    private static final int MAX = 5;
    private static final Semaphore SEMAPHORE = new Semaphore(MAX, true);

    public static void main(String[] args) {
        final var executorService = Executors.newFixedThreadPool(9);

        for (int i = 0 ; i < 50 ; i++) {
            executorService.submit(createRunnableTask(i));
        }

        executorService.shutdown();
    }

    public static void doSomeTask(int i) throws InterruptedException {
        final var threadName = Thread.currentThread().getName();
        getPermit(threadName, i);
        releasePermit(threadName, i);
    }

    public static void getPermit(String threadName, int i) throws InterruptedException {
        System.out.println(threadName + " trying to acquire semaphore permit for task " + i);
        SEMAPHORE.acquire();
        System.out.println(threadName + " acquired semaphore permit for task " + i);
    }

    public static void releasePermit(String threadName, int i) {
        SEMAPHORE.release();
        System.out.println(threadName + " released semaphore permit for task " + i);
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
