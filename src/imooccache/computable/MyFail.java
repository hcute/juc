package imooccache.computable;

import java.io.IOException;

public class MyFail implements Computable<String,Integer>{

    @Override
    public Integer compute(String args) throws Exception {
        double random = Math.random();
        if (random > 0.5) {
            throw new IOException("读取文件出错");
        }
        Thread.sleep(3000);
        return Integer.valueOf(args);
    }
}
