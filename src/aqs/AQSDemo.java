package aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 *
 */
public class AQSDemo {
    public static void main(String[] args) {
        new Semaphore(3);
        new CountDownLatch(10);
    }
}
