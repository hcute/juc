package imooccache;

import imooccache.computable.Computable;
import imooccache.computable.ExpensiveFunction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用装饰者模式实现缓存 存在重复计算的问题
 */
public class ImoocCache6<A,V> implements Computable<A,V> {

    private final ConcurrentHashMap<A,V> cache = new ConcurrentHashMap<>();

    private Computable<A,V> computable;

    public ImoocCache6(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public  V compute(A args) throws Exception {
        System.out.println("进入缓存机制");
        // 这里多线程会存在重复计算的情况
        V result = cache.get(args);
        if (result == null) {
            System.out.println("计算了" + args);
            result = computable.compute(args);
            cache.put(args,result);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache6<String, Integer> expensiveCompute = new ImoocCache6<>(new ExpensiveFunction());

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
                    Integer result = expensiveCompute.compute("667");
                    System.out.println("第三次执行的结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
