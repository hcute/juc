package collections.predcessor;

import java.util.Hashtable;

public class HashtableDemo {

    public static void main(String[] args) {
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("学完以后跳槽涨薪幅度","80%");
        System.out.println("学完后跳槽涨薪幅度：" + hashtable.get("学完以后跳槽涨薪幅度"));
    }
}
