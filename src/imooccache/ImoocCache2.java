package imooccache;

import imooccache.computable.Computable;
import imooccache.computable.ExpensiveFunction;

import java.util.HashMap;

/**
 * 使用装饰者模式实现缓存
 */
public class ImoocCache2<A,V> implements Computable<A,V> {

    private final HashMap<A,V> cache = new HashMap<>();

    private Computable<A,V> computable;

    public ImoocCache2(Computable<A, V> computable) {
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
        ImoocCache2<String, Integer> expensiveCompute = new ImoocCache2<>(new ExpensiveFunction());
        Integer compute = expensiveCompute.compute("666");
        System.out.println("第一次计算结果：" + compute);
        compute = expensiveCompute.compute("666");
        System.out.println("第二次计算结果：" + compute);

    }
}
