package threadlocal;

/**
 * ThreadLocal发生空指针异常
 */
public class ThreadLocalNPE {
    ThreadLocal<Long> longThreadLocal = new ThreadLocal<>();

    public void set() {
        longThreadLocal.set(Thread.currentThread().getId());
    }

    // 拆箱可能发生nullPointException
    public long get(){
        return longThreadLocal.get();
    }

    public static void main(String[] args) {
        ThreadLocalNPE threadLocalNPE = new ThreadLocalNPE();
        System.out.println(threadLocalNPE.get());
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadLocalNPE.set();
                long l = threadLocalNPE.get();
                System.out.println(l);
            }
        }).start();


    }
}
