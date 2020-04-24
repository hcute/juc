package lock.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  演示悲观锁和乐观锁
 */
public class PessimismOptimismLock {

    int a;

    /**
     * 乐观锁
     * @param args
     */
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet();
    }

    /**
     * 悲观锁
     */
    public synchronized void testMethod(){
        a++;
    }
}
