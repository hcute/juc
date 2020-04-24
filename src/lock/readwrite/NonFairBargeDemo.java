package lock.readwrite;


import jdk.nashorn.internal.ir.CallNode;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 公平和非公平锁中的读锁和写锁是否可以插队
 * 读锁在等待队列的头节点不是写的操作的时候可以插队
 */
public class NonFairBargeDemo {
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
    private static ReentrantReadWriteLock.ReadLock readLock =  reentrantReadWriteLock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void read(){
        System.out.println(Thread.currentThread().getName()+"开始尝试获取读锁");
        readLock.lock();

        try {
            System.out.println(Thread.currentThread().getName()+"获取读锁,正在读取");
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName()+"释放读锁");
            readLock.unlock();
        }
    }


    public static void write(){
        System.out.println(Thread.currentThread().getName()+"开始尝试获取写锁");
        writeLock.lock();

        try {
            System.out.println(Thread.currentThread().getName()+"获取写锁,正在写入");
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName()+"释放写锁");
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> write(),"Thread1").start();
        new Thread(() -> read(),"Thread2").start();
        new Thread(() -> read(),"Thread3").start();
        new Thread(() -> write(),"Thread4").start();
        new Thread(() -> read() ,"Thread5").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread thread[] = new Thread[1000];
                for (int i = 0; i < 1000 ; i++) {
                    thread[i] = new Thread(()->{read();},"子线程创建的Thread" + i);
                }
                for (int i = 0; i < 1000; i++) {
                    thread[i].start();
                }
            }
        }).start();
    }

}
