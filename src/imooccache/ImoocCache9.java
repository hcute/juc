package imooccache;

import imooccache.computable.Computable;
import imooccache.computable.MyFail;

import java.util.Map;
import java.util.concurrent.*;

/**
 *
 * 利用Future解决重复计算的问题，但是还是存在小概率的重复计算
 */
public class ImoocCache9<A,V> implements Computable<A,V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private Computable<A,V> computable;

    public ImoocCache9(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public  V compute(A args) throws Exception{
        while (true) {
            Future<V> vFuture = cache.get(args);
            if (vFuture == null) {
                Callable<V> callable = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return computable.compute(args);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(callable);
                vFuture = cache.putIfAbsent(args, ft);
                // 解决小概率的重复计算问题
                if (vFuture == null) {
                    vFuture = ft;
                    System.out.println("调用了FutureTask进行计算");
                    ft.run();
                }
            }
            try {
                return vFuture.get();
            } catch (CancellationException e){
                System.out.println("被取消了");
                cache.remove(args);
                throw e;
            } catch (InterruptedException e) {
                cache.remove(args);
                System.out.println("被中断了");
                throw e;
            } catch (ExecutionException e) {
                cache.remove(args);
                System.out.println("执行抛出了异常，重试");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ImoocCache9<String, Integer> expensiveCompute = new ImoocCache9<>(new MyFail());

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
//        Future<Integer> integerFuture = expensiveCompute.cache.get("666");
//        integerFuture.cancel(true);

    }
}
