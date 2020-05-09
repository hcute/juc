package imooccache.decorator.v2;

public class Test {

    public static void main(String[] args) {
        APancake aPancake = new Pancake();
        aPancake = new EggDecorator(aPancake);
        aPancake = new EggDecorator(aPancake);
        aPancake = new SausageDecorator(aPancake);

        System.out.println(aPancake.desc() +"的价格：" + aPancake.cost());
    }
}
