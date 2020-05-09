package imooccache;

import imooccache.computable.Computable;
import imooccache.computable.MyFail;

import java.util.Map;
import java.util.concurrent.*;

/**
 *
 * 利用scheduledThreadPool线程池，实现缓存
 */
public class ImoocCache10<A,V> implements Computable<A,V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private Computable<A,V> computable;

    public ImoocCache10(Computable<A, V> computable) {
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
    public final static ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    public V compute(A args,long expire) throws Exception {
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                expire(args);
            }
        },expire,TimeUnit.MILLISECONDS);
        executor.shutdown();
        return compute(args);
    }

    public synchronized void expire(A key) {
        Future<V> vFuture = cache.get(key);
        if (vFuture != null) {
            if(!vFuture.isDone()){
                System.out.println("Future任务被取消了");
                vFuture.cancel(true);
            }
            System.out.println("缓存时间到期，被清理");
            cache.remove(key);
        }
    }

    /**
     * 防止缓存雪崩，添加随机时间
     * @param args
     * @return
     */
    public V computeExpireRandom(A args) throws Exception {
        long expire = (long) (Math.random()*10000L);
        return compute(args,expire);
    }

    public static void main(String[] args) throws Exception {
        ImoocCache10<String, Integer> expensiveCompute = new ImoocCache10<>(new MyFail());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = expensiveCompute.compute("666",5000L);
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
                    Integer result = expensiveCompute.compute("666",5000L);
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

        Thread.sleep(9000L);
        Integer result = expensiveCompute.compute("666");
        System.out.println("主线程执行的结果：" + result);
    }
}
