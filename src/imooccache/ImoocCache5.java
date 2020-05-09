package imooccache;

import imooccache.computable.Computable;
import imooccache.computable.ExpensiveFunction;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用装饰者模式实现缓存 存在重复计算的问题
 */
public class ImoocCache5<A,V> implements Computable<A,V> {

    private final ConcurrentHashMap<A,V> cache = new ConcurrentHashMap<>();

    private Computable<A,V> computable;

    public ImoocCache5(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public  V compute(A args) throws Exception {
        System.out.println("进入缓存机制");
        // 这里多线程会存在重复计算的情况
        V result = cache.get(args);
        if (result == null) {
            result = computable.compute(args);
            cache.put(args,result);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache5<String, Integer> expensiveCompute = new ImoocCache5<>(new ExpensiveFunction());

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
                    Integer result = expensiveCompute.compute("667");
                    System.out.println("第二次执行的结果：" + result);
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
    }
}
