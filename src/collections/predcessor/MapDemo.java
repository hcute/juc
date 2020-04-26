package collections.predcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示map的常用方法
 */
public class MapDemo {
    public static void main(String[] args) {
        Map map = new HashMap<String,Integer>();
        System.out.println(map.isEmpty());
        map.put("东哥",38);
        map.put("西哥",28);
        map.put(null,null);
        System.out.println(map.keySet());
        System.out.println(map.get(null));
        System.out.println(map.size());
        System.out.println(map.containsKey(null));
        map.remove(null);
        System.out.println(map.containsKey(null));

    }
}
