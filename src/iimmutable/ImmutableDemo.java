package iimmutable;

import java.util.HashSet;
import java.util.Set;

/**
 * 有引用类型的对象
 */
public class ImmutableDemo {
    private final Set<String> students = new HashSet<>();

    public ImmutableDemo() {
        students.add("李小美");
        students.add("王庄");
        students.add("徐福记");
    }

    public boolean isStudent(String name) {
        return students.contains(name);
    }
}
