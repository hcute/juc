package cas;

/**
 * 模拟cas操作，等价代码
 */
public class TwoThreadCompetition implements Runnable{

    private volatile int value;


    /**
     * 相当于CAS的指令
     * @param expectedValue
     * @param newValue
     * @return
     */
    public synchronized int compareAndSwap(int expectedValue,int newValue){
        int oldValue = value;
        if (oldValue == expectedValue){
            value = newValue;
        }
        return value;
    }

    public static void main(String[] args) throws InterruptedException {
        TwoThreadCompetition r = new TwoThreadCompetition();
        r.value = 0;
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(r.value);
    }

    @Override
    public void run() {
        compareAndSwap(0,1);
    }
}
