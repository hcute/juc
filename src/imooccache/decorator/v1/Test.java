package imooccache.decorator.v1;

/**
 * 批量替换变量名称的快捷键 shift + F6
 */
public class Test {

    public static void main(String[] args) {
        Pancake littlePancake = new Pancake();
        System.out.println(littlePancake.desc() + "的价格：" + littlePancake.cost());
        Pancake middlePancake = new PancakeWithEgg();
        System.out.println(middlePancake.desc() + "的价格：" + middlePancake.cost());
        Pancake bigPancake = new PancakeWithEggSausage();
        System.out.println(bigPancake.desc() + "的价格：" + bigPancake.cost());

    }

}
