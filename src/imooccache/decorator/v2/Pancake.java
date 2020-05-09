package imooccache.decorator.v2;

public class Pancake extends APancake{
    @Override
    protected String desc() {
        return "煎饼";
    }

    @Override
    protected int cost() {
        return 8;
    }

}
