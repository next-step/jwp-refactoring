package kitchenpos.product.exception;

import kitchenpos.exception.KitchenPosArgumentException;

public class IllegalProductException extends KitchenPosArgumentException {
    public IllegalProductException(String s) {
        super(s);
    }
}
