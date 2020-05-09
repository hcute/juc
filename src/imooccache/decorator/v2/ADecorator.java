package imooccache.decorator.v2;

public abstract class ADecorator extends APancake{

    private APancake aPancake;

    public ADecorator(APancake aPancake) {
        this.aPancake = aPancake;
    }

    @Override
    protected String desc() {
        return aPancake.desc();
    }

    @Override
    protected int cost() {
        return aPancake.cost();
    }
}
