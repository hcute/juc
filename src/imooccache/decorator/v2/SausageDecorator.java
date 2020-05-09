package imooccache.decorator.v2;

public class SausageDecorator extends ADecorator{

    public SausageDecorator(APancake aPancake) {
        super(aPancake);
    }

    @Override
    protected String desc() {
        return super.desc() + " 加一根香肠";
    }

    @Override
    protected int cost() {
        return super.cost() + 2;
    }
}
