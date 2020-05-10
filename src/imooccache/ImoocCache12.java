package imooccache;

import imooccache.computable.ExpensiveFunction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试缓存的效率
 */
public class ImoocCache12 {


    static ImoocCache10<String,Integer> cache = new ImoocCache10<>(new ExpensiveFunction());


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10000);

        // 不能创建更多的线程
        long start = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            executorService.submit(() -> {
                Integer compute = null;
                try {
                     compute = cache.compute("666");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(compute);
            });
        }
        executorService.shutdown();
        while (!executorService.isTerminated()){}
        System.out.println("总耗时" + (System.currentTimeMillis() - start) );


    }
}
