package iimmutable;

public class WiuKong2 {

    public static void main(String[] args) {
        String a = "wukong2";
        // 编译器无法优化
        final String b = getDaShiXiong();
        final String d = "wukong";
        String c = b + 2;
        String e = d + 2;
        System.out.println(a == c);
        System.out.println(a == e);
    }

    private static String getDaShiXiong() {
        return "wuKong";
    }
}
