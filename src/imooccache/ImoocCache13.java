package imooccache;

import imooccache.computable.ExpensiveFunction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试缓存的效率 ，使用CountDownLatch
 */
public class ImoocCache13 {


    static ImoocCache10<String, Integer> cache = new ImoocCache10<>(new ExpensiveFunction());
    static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10000);

        // 不能创建更多的线程
        long start = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            executorService.submit(() -> {
                Integer compute = null;
                try {
                    System.out.println(Thread.currentThread().getName() + "开始等待");
                    countDownLatch.await();
                    SimpleDateFormat simpleDateFormat = SafeSimpleDataFormat.dateFormat.get();
                    String time = simpleDateFormat.format(new Date());

                    System.out.println(Thread.currentThread().getName() + "   " + time + " 被放行");

                    compute = cache.compute("666");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(compute);
            });
        }
        Thread.sleep(5000);
        countDownLatch.countDown();
        executorService.shutdown();
    }
}

class SafeSimpleDataFormat{
    public static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("mm:ss");
        }

        @Override
        public SimpleDateFormat get() {
            return super.get();
        }
    };
}
