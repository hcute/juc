package collections.copyonwrite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 演示CopyOnWriteArrayList可以在迭代过程中修改数组的内容，但是ArrayList不行
 * CopyOnWriteArrayList 修改和迭代的是不同的list
 */
public class CopyOnWriteArrayListDemo1 {

    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<>();
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        Iterator<String> iterator = list.iterator();

        //迭代不可中断
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
            System.out.println("list is " + list);

            if (next.equals("2")){
                list.remove("5");
            }
            if (next.equals("3")){
                list.add("3 found");
            }
        }
    }
}
