package flowControl.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟跑步比赛 。开始的时候运动员等待裁判的发令枪，到达重点店的时候裁判等待运动员告知我已经跑完了
 *
 */
public class CountDownLatchDemo1And2 {

    public static void main(String[] args) throws InterruptedException {
        // 裁判的枪
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("No." + no + "准备完毕，等待发令枪");
                    try {
                        begin.await();
                        System.out.println("No." + no + "开始跑步");
                        Thread.sleep((long)(Math.random()*10000));
                        System.out.println("No." + no + "跑到终点了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        end.countDown();
                    }
                }
            };

            executorService.submit(runnable);

        }

        Thread.sleep(5000);
        System.out.println("发令枪响起，比赛开始！");
        begin.countDown();

        end.await();
        System.out.println("所有人到达终点，比赛结束");
        executorService.shutdown();

    }
}
