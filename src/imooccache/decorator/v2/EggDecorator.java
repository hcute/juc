package imooccache.decorator.v2;

public class EggDecorator extends ADecorator {

    public EggDecorator(APancake aPancake) {
        super(aPancake);
    }

    @Override
    protected String desc() {
        return super.desc() + " 加一个鸡蛋";
    }

    @Override
    protected int cost() {
        return super.cost() + 1;
    }
}
