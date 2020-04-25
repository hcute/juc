package iimmutable;

public class WuKong {
    public static void main(String[] args) {
        String a = "wukong2";
        final String b = "wukong";
        String c = "wukong";
        String d = b + 2;
        String e = c + 2;
        System.out.println(a == d);
        System.out.println(a == e);

    }
}
