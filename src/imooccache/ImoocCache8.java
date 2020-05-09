package imooccache;

import imooccache.computable.Computable;
import imooccache.computable.ExpensiveFunction;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 *
 * 利用Future解决重复计算的问题，但是还是存在小概率的重复计算
 */
public class ImoocCache8<A,V> implements Computable<A,V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private Computable<A,V> computable;

    public ImoocCache8(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public  V compute(A args) throws Exception {
        Future<V> vFuture = cache.get(args);
        if (vFuture == null) {
            Callable<V> callable = new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return computable.compute(args);
                }
            };
            FutureTask<V> ft = new FutureTask<>(callable);
            vFuture= cache.putIfAbsent(args, ft);
            // 解决小概率的重复计算问题
            if (vFuture == null) {
                vFuture = ft;
                System.out.println("调用了FutureTask进行计算");
                ft.run();
            }
        }
        return vFuture.get();
    }

    public static void main(String[] args) throws Exception {
        ImoocCache8<String, Integer> expensiveCompute = new ImoocCache8<>(new ExpensiveFunction());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = expensiveCompute.compute("666");
                    System.out.println("第一次执行的结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = expensiveCompute.compute("666");
                    System.out.println("第三次执行的结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = expensiveCompute.compute("667");
                    System.out.println("第二次执行的结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
