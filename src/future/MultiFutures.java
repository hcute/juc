package future;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 批量提交任务，用list批量接收结果
 */
public class MultiFutures {


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        List<Future<Integer>> futures = new ArrayList<>();
        Callable<Integer> callable = () -> {
            Thread.sleep(3000);
            return new Random().nextInt();
        };
        for (int i = 0; i < 20; i++) {
            Future<Integer> future = executorService.submit(callable);
            futures.add(future);
        }

        for (int i = 0; i < 20; i++) {
            try {
                Integer integer = futures.get(i).get();
                System.out.println(integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
