package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁升降级
 */
public class Upgrading {

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);

    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private static void readUpgrading (){
        readLock.lock();

        try {
            System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
            Thread.sleep(1000);
            System.out.println("升级不会成功带来阻塞");
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + "获取到写锁，升级成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放了读锁");
            readLock.unlock();

        }
    }

    private static void writeDowngrading(){
        writeLock.lock();

        try {
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在读取");
            Thread.sleep(1000);
            readLock.lock();
            System.out.println("在不释放写锁的情况下，直接获取到读锁，成功降级");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放了写锁");
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("演示降级是可以的");
        Thread thread = new Thread(() -> writeDowngrading());
        thread.start();
        thread.join();
        System.out.println("-------------");
        System.out.println("演示升级是不可以的");
        new Thread(() -> readUpgrading()).start();
    }
}
