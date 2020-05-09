package imooccache.computable;

import java.util.concurrent.TimeUnit;

public class ExpensiveFunction implements Computable<String,Integer> {

    @Override
    public Integer compute(String args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return Integer.valueOf(args);
    }
}
