package flowControl.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 普通用法 绑定在锁上面
 */
public class ConditionDemo1 {

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    void method1(){
        lock.lock();

        try {
            System.out.println("条件不满足，开始await");

            condition.await();

            System.out.println("条件满足了，开始执行后续任务");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    void method2(){
        lock.lock();

        try {
            System.out.println("准备工作完成了，唤醒其他线程");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        ConditionDemo1 conditionDemo1 = new ConditionDemo1();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                conditionDemo1.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        conditionDemo1.method1();
    }
}
