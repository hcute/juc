package aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class AQSDemo {
    public static void main(String[] args) {
        new Semaphore(3);
        new CountDownLatch(10);
        new ReentrantLock();
    }
}
