package kitchenpos.exception;

public class IllegalOrderLineItemException extends KitchenPosArgumentException{
    public IllegalOrderLineItemException(String s) {
        super(s);
    }
}
