package kitchenpos.ordertable.exception;

import kitchenpos.exception.KitchenPosArgumentException;

public class IllegalOrderTableException extends KitchenPosArgumentException {
    public IllegalOrderTableException(String s) {
        super(s);
    }
}
