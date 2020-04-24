package lock.reentrantlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示ReentrantLock 被打断的场景
 */
public class LockDemo {

    static class Outputer {
        Lock lock = new ReentrantLock();

        public void output(String name) {
            int len = name.length();
            lock.lock();
            try {
                for (int i = 0; i < len; i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println("");
            } finally {
                lock.unlock();
            }
        }
    }

    private void init() {
        final Outputer outputer = new Outputer();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputer.output("悟空");
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputer.output("大师兄");
            }
        }).start();

    }

    public static void main(String[] args) {
        new LockDemo().init();
    }
}
