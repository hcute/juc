package lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示电影院选座位
 */
public class CinemaBookSeat {

    private static ReentrantLock lock = new ReentrantLock();

    private static void bootSeat() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "开始预定座位");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + "完成预定座位 ");
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {bootSeat();}).start();
        new Thread(() -> {bootSeat();}).start();
        new Thread(() -> {bootSeat();}).start();
        new Thread(() -> {bootSeat();}).start();
    }

}
