import java.util.HashMap;

/**
 * 演示HashMap在多线程情况下造成的死循环问题
 * 详细说明地址：https://coolshell.cn/articles/9606.html
 * 概念性的结论：多线程扩容的时候可能会导致循环链表，导致cpu 100%占用
 */
public class HashMapEndlessLoop {
    private static HashMap<Integer,String> map = new HashMap<Integer,String>(2,1.5f);

    public static void main(String[] args) {
        map.put(5,"C");
        map.put(7,"B");
        map.put(3,"A");
        new Thread(new Runnable() {
            @Override
            public void run() {
                map.put(15,"D");
                System.out.println(map);
            }
        },"Thread-1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                map.put(1,"E");
                System.out.println(map);
            }
        },"Thread-2").start();
    }

}
