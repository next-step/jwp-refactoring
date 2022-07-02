package kitchenpos.order.exception;

import kitchenpos.exception.KitchenPosArgumentException;

public class IllegalOrderException extends KitchenPosArgumentException {
    public IllegalOrderException(String s) {
        super(s);
    }
}
