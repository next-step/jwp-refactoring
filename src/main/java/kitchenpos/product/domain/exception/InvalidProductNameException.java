package kitchenpos.product.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class InvalidProductNameException extends BusinessException {
    public InvalidProductNameException() {
        super("상품이름을 지정해야합니다.");
    }

    public InvalidProductNameException(String message) {
        super(message);
    }
}
