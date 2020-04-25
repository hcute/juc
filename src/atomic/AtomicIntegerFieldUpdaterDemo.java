package atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 演示AtomicIntegerFieldUpdater的用法
 */
public class AtomicIntegerFieldUpdaterDemo implements Runnable{

    static Candidate tom;
    static Candidate peter;


    public static AtomicIntegerFieldUpdater<Candidate> scoreUpdater =
            AtomicIntegerFieldUpdater.newUpdater(Candidate.class,"score");
    public static class Candidate {
        volatile int score;

    }


    @Override
    public void run() {
        for (int i = 0; i < 10000 ; i++) {
            peter.score++;
            scoreUpdater.getAndIncrement(tom);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerFieldUpdaterDemo atomicIntegerFieldUpdaterDemo = new AtomicIntegerFieldUpdaterDemo();
        tom = new Candidate();
        peter = new Candidate();
        Thread t1 = new Thread(atomicIntegerFieldUpdaterDemo);
        Thread t2 = new Thread(atomicIntegerFieldUpdaterDemo);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("普通变量：" + peter.score);
        System.out.println("升级后的结果：" + tom.score);

    }
}
