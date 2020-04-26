package collections.predcessor;

import java.util.Vector;

/**
 * 演示Vector，看Vector源码
 */
public class VectorDemo {

    public static void main(String[] args) {
        Vector<String> strings = new Vector<>();
        strings.add("test");
        System.out.println(strings.get(0));
    }
}
