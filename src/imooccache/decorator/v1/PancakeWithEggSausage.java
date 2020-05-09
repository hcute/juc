package imooccache.decorator.v1;

public class PancakeWithEggSausage extends PancakeWithEgg {

    @Override
    protected String desc() {
        return super.desc() + " 加一根香肠";
    }

    @Override
    protected int cost() {
        return super.cost() + 2;
    }

}
