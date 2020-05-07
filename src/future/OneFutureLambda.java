package future;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 演示一个Future的使用方法
 */
public class OneFutureLambda {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Callable<Integer> callable = () -> {
            Thread.sleep(3000);
            return new Random().nextInt();
        };
        Future<Integer> submit = executorService.submit(callable);

        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }


}
