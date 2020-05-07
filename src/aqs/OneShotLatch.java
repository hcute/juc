package aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 实现简单的门闩，利用AQS
 */
public class OneShotLatch {


    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(0);
    }

    public void await(){
        sync.acquireShared(0);
    }

    private class Sync extends AbstractQueuedSynchronizer{

        @Override
        protected int tryAcquireShared(int arg) {
            return getState() == 1 ? 1 : -1;
        }
        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OneShotLatch oneShotLatch = new OneShotLatch();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "尝试获取Latch，获取失败就等待");

                    oneShotLatch.await();

                    System.out.println(Thread.currentThread().getName() + "放闸，继续执行");
                }
            }).start();
        }

        Thread.sleep(5000);
        oneShotLatch.signal();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "尝试获取Latch，获取失败就等待");

                oneShotLatch.await();

                System.out.println(Thread.currentThread().getName() + "放闸，继续执行");
            }
        }).start();

    }
}
