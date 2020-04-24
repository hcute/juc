package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 非公平锁下读不可以插队
 */
public class CinemaReadWrite {

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);

    private static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    private static void read(){
        readLock.lock();

        try {
            System.out.println(Thread.currentThread().getName() + "得到了读锁，正在读取");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了读锁");
            readLock.unlock();

        }
    }

    private static void write(){
        writeLock.lock();

        try {
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在读取");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了写锁");
            writeLock.unlock();

        }
    }

    public static void main(String[] args) {
        new Thread(() -> write(),"Thread1").start();
        new Thread(() -> read(),"Thread2").start();
        new Thread(() -> read(),"Thread3").start();
        new Thread(() -> write(),"Thread4").start();
        new Thread(() -> read() ,"Thread5").start();
    }
}
