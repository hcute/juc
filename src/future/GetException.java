package future;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.Random;
import java.util.concurrent.*;

/**
 * get方法过程中抛出异常，执行get的时候才会抛出异常
 */
public class GetException {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Future<Integer> submit = executorService.submit(new CallableTask());

        try {
            for (int i = 0; i < 5; i++) {
                System.out.println(i);
                Thread.sleep(500);
            }

            System.out.println(submit.isDone());
            submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("抛出InterruptedException");
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.out.println("抛出ExecutionException");

        }

    }

    static class CallableTask implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            throw new IllegalArgumentException("Callable抛出了异常");
        }
    }

}
