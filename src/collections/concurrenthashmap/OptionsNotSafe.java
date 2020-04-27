package collections.concurrenthashmap;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 组合操作并不保证线程安全
 */
public class OptionsNotSafe implements Runnable{

    private static  ConcurrentHashMap<String,Integer> scores = new ConcurrentHashMap<String,Integer>();


    public static void main(String[] args) throws InterruptedException {
        scores.put("小明",0);
        OptionsNotSafe r = new OptionsNotSafe();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(scores);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            // get和put组合操作不是线程安全的,如果使用synchronized有不建议
//            synchronized (OptionsNotSafe.class) {
//                Integer score = scores.get("小明");
//                Integer newScore = score + 1;
//                scores.put("小明",newScore);
//            }

            // 可以用replace方法替代
            while (true) {
                Integer score = scores.get("小明");
                Integer newScore = score + 1;
                boolean b = scores.replace("小明", score, newScore);
                if (b) {
                    break;
                }
            }
        }
    }
}
