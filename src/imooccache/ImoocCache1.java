package imooccache;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 这个版本存在线程安全问题：
 *      解决方法1：如果直接在computer方法上加上synchronized
 *          会存在性能问题
 *          存在复用性问题
 *
 */
public class ImoocCache1 {

    private final HashMap<String,Integer> cache = new HashMap<>();

    public synchronized Integer computer(String userId) throws InterruptedException {
        Integer result = cache.get(userId);
        // 先检查cache中是否存在，如果存在直接返回，如果不存在就进行计算
        if (result == null) {
            result = doCompute(userId);
            cache.put(userId,result);
        }
        return result;
    }


    public Integer doCompute(String userId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return new Integer(userId);
    }

    public static void main(String[] args) throws InterruptedException {
        ImoocCache1 imoocCache1 = new ImoocCache1();
        System.out.println("开始计算了");
        Integer computer1 = imoocCache1.computer("13");
        System.out.println("第一次的计算结果：" + computer1);
        Integer computer2 = imoocCache1.computer("13");
        System.out.println("第二次的计算结果：" + computer2);
    }

}
