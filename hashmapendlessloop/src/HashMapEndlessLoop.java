import sun.management.snmp.jvminstr.JvmRTInputArgsEntryImpl;

import java.util.HashMap;

/**
 * 演示HashMap在多线程情况下造成的死循环问题
 */
public class HashMapEndlessLoop {
    private static HashMap<Integer,String> map = new HashMap<Integer,String>(2,1.5f);

    public static void main(String[] args) {
    }

}
