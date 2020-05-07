package future;

import java.util.concurrent.*;

/**
 * 演示get的超时方法，超时之后需要取消任务，cancel方法传入true和false的区别，表示是否中断正在执行的任务
 */
public class Timeout {

    private static final Ad DEFAULT_AD = new Ad("无网络的时候的默认广告");

    private static final ExecutorService exec = Executors.newFixedThreadPool(10);

    static class Ad{
        String name;
        public Ad(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return "Ad{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    static class FetchAdTask implements Callable<Ad>{

        @Override
        public Ad call() throws Exception {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("sleep期间被中断了");
                return new Ad("被中断时候的默认广告");
            }
            return new Ad("旅游订票哪家强？找携程");
        }
    }

    public void printAd(){
        Future<Ad> f = exec.submit(new FetchAdTask());
        Ad ad;
        try {
            ad = f.get(2000,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            ad = new Ad("被中断的时候的默认广告");
            e.printStackTrace();
        } catch (ExecutionException e) {
            ad = new Ad("异常时候的默认广告");
            e.printStackTrace();
        } catch (TimeoutException e) {
            ad = new Ad("超时时候的默认广告");
            System.out.println("超时，未获取到广告");
            boolean cancel = f.cancel(true);
            System.out.println("cancel的结果，" + cancel);

        }
        exec.shutdown();
        System.out.println(ad);
    }

    public static void main(String[] args) {
        new Timeout().printAd();
    }
}
