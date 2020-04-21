package lock.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock 不会像synchronized一样 发生异常自动释放锁，所以最佳实践是，在finally代码块中释放锁
 */
public class MustUnLock {

    private static Lock lock = new ReentrantLock();


    public static void main(String[] args) {
        lock.lock();
        try {
            // 获取本锁保护的资源
            System.out.println(Thread.currentThread().getName() +"开始执行任务");
        }finally {
            lock.unlock();
        }
    }
}
