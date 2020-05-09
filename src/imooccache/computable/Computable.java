package imooccache.computable;

/**
 * 耗时计算的业务接口类 被装饰者
 */
public interface Computable<A,V> {

    V compute(A args) throws Exception;
}
