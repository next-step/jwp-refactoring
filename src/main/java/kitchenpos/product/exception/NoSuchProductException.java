package kitchenpos.product.exception;

import kitchenpos.exception.KitchenPosArgumentException;

public class NoSuchProductException extends KitchenPosArgumentException {
    private static final String ERROR_MESSAGE = "Product가 존재하지 않습니다 (id: %d)";

    public NoSuchProductException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
