package imooccache;

import imooccache.computable.Computable;
import imooccache.computable.ExpensiveFunction;

import java.util.HashMap;

/**
 * 使用装饰者模式实现缓存
 */
public class ImoocCache3<A,V> implements Computable<A,V> {

    private final HashMap<A,V> cache = new HashMap<>();

    private Computable<A,V> computable;

    public ImoocCache3(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public synchronized V compute(A args) throws Exception {
        System.out.println("进入缓存机制");
        V result = cache.get(args);
        if (result == null) {
            result = computable.compute(args);
            cache.put(args,result);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        ImoocCache3<String, Integer> expensiveCompute = new ImoocCache3<>(new ExpensiveFunction());

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
