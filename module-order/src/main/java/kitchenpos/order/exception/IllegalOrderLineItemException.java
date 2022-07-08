package kitchenpos.order.exception;

public class IllegalOrderLineItemException extends KitchenPosArgumentException {
    public IllegalOrderLineItemException(String s) {
        super(s);
    }
}
