package imooccache.decorator.v1;

public class PancakeWithEgg extends Pancake{

    @Override
    protected String desc() {
        return super.desc() + " 加一个鸡蛋";
    }

    @Override
    protected int cost() {
        return super.cost() + 1;
    }
}
