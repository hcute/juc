package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  演示newFixedThreadPool
 *      - 里面的workQueue采用的是LinkedBlockingQueue 的无界队列
 *      - 随着任务的增多会导致oom错误
 */
public class FixedThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Task());
        }

    }

}

class Task implements Runnable{

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}
