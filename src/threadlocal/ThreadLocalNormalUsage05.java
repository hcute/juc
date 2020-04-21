package threadlocal;

/**
 * 解决同一个线程内多个方法共享数据
 */
public class ThreadLocalNormalUsage05 {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Service1().process("超哥" + finalI);
                }
            }).start();
        }
        new Service1().process("超哥");
    }

}

class Service1 {
    public void process(String username) {
        User user = new User(username);
        UserContextHolder.holder.set(user);
        Service2 service2 = new Service2();
        service2.process();
        Service3 service3 = new Service3();
        service3.process();
    }
}

class Service2 {
    public void process() {
        User user = UserContextHolder.holder.get();
        System.out.println("service2 :" + user.username);
        UserContextHolder.holder.remove();
    }
}

class Service3 {
    public void process() {
        UserContextHolder.holder.set(new User("王姐"));
        User user = UserContextHolder.holder.get();
        System.out.println("service3 :" + user.username);
        // 避免内存泄漏
        UserContextHolder.holder.remove();
    }
}
class UserContextHolder {
    public static ThreadLocal<User> holder = new ThreadLocal<>();
}

class User{
    String username;

    public User(String username) {
        this.username = username;
    }
}