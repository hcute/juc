package collections.copyonwrite;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo2 {

    public static void main(String[] args) {

        CopyOnWriteArrayList<Integer> integers = new CopyOnWriteArrayList<>(new Integer[]{1, 2, 3});

        System.out.println(integers);

        Iterator<Integer> itr1 = integers.iterator();

        integers.add(4);

        System.out.println(integers);

        Iterator<Integer> itr2 = integers.iterator();

        itr1.forEachRemaining(System.out::println);
        itr2.forEachRemaining(System.out::println);


    }
}
