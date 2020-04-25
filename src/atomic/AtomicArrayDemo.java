package atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArrayDemo {

    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(1000);

        Decrementer decrementer = new Decrementer(atomicIntegerArray);
        Incrementer incrementer = new Incrementer(atomicIntegerArray);

        Thread[] threadsIncrementer = new Thread[100];
        Thread[] threadsDecrementer = new Thread[100];

        for (int i = 0; i < 100; i++) {
            threadsIncrementer[i] = new Thread(incrementer);
            threadsDecrementer[i] = new Thread(decrementer);

            threadsDecrementer[i].start();
            threadsIncrementer[i].start();

        }
        for (int i = 0; i < 100 ; i++) {
            threadsDecrementer[i].join();
            threadsIncrementer[i].join();
        }

//        Thread.sleep(10000);
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            if (atomicIntegerArray.get(i) != 0 ){
                System.out.println("发现了非零值Incrementer，发生了错误");
            }
        }
        System.out.println("运行结束");
    }
}


class Decrementer implements Runnable{
    private AtomicIntegerArray array;

    public Decrementer(AtomicIntegerArray array) {
        this.array = array;
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length(); i++) {
            array.getAndDecrement(i);
        }
    }
}

class Incrementer implements Runnable{
    private AtomicIntegerArray array;

    public Incrementer(AtomicIntegerArray array) {
        this.array = array;
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length(); i++) {
            array.getAndIncrement(i);
        }
    }
}
