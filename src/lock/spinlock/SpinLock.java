package lock.spinlock;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLock {

    private AtomicReference<Thread> sign = new AtomicReference();


    public void lock(){
        Thread current = Thread.currentThread();
        while (!sign.compareAndSet(null,current)) {
            System.out.println("获取自旋锁失败，正在尝试");
        }
    }

    public void unlock(){
        Thread currentThread = Thread.currentThread();
        sign.compareAndSet(currentThread,null);
    }

    public static void main(String[] args) {
        SpinLock spinLock = new SpinLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始尝试获取自旋锁");
                spinLock.lock();
                System.out.println(Thread.currentThread().getName() + "获取到了自旋锁");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    spinLock.unlock();
                    System.out.println(Thread.currentThread().getName() + "释放了自旋锁");

                }
            }
        };
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
