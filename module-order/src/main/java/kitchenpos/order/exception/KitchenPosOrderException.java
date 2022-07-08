package kitchenpos.order.exception;

public abstract class KitchenPosOrderException extends RuntimeException{
    public KitchenPosOrderException(String s) {
        super(s);
    }
}
