package cas;

/**
 * 模拟cas操作，等价代码
 */
public class SimulatedCAS {

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
}
