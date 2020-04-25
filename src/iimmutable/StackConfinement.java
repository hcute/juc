package iimmutable;

/**
 * 演示基本变量和对象
 *  演示线程争抢发生错误 ，放入方法内问题解决
 */
public class StackConfinement implements Runnable{
    int index = 0;
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            index++;
        }
        inThread();
    }

    public void inThread(){
        int neverGoOut = 0;
        for (int i = 0; i < 10000; i++) {
            neverGoOut++;
        }
        System.out.println("栈内保护的数字是线程安全的：" + neverGoOut);
    }

    public static void main(String[] args) throws InterruptedException {
        StackConfinement stackConfinement = new StackConfinement();
        Thread t1 = new Thread(stackConfinement);
        Thread t2 = new Thread(stackConfinement);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(stackConfinement.index);
    }
}
