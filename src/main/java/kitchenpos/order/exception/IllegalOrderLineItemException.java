package kitchenpos.order.exception;

import kitchenpos.exception.KitchenPosArgumentException;

public class IllegalOrderLineItemException extends KitchenPosArgumentException {
    public IllegalOrderLineItemException(String s) {
        super(s);
    }
}
