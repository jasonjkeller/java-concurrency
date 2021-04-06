package synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;

/**
 * A synchronization point at which threads can exchange data.
 */
public class ExchangerExample {

    private static final Exchanger<String> EXCHANGER = new Exchanger<>();

    public static void main(String[] args) {
        final var executorService = Executors.newFixedThreadPool(2);

        for (int i = 0 ; i < 2 ; i++) {
            executorService.submit(createRunnableTask(i));
        }

        executorService.shutdown();
    }

    public static void doSomeTask(int i) throws InterruptedException, BrokenBarrierException {
        final var threadName = Thread.currentThread().getName();
        System.out.println("Hello from " + threadName + " for task " + i);
        final var exchangedName = EXCHANGER.exchange(threadName);
        System.out.println(threadName + " says: Hello " + exchangedName);
        final var exchangedTask = EXCHANGER.exchange("task " + i);
        System.out.println(threadName + " says: I see you are working on task " + exchangedTask);
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
