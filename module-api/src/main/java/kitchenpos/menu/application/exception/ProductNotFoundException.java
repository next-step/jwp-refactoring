package kitchenpos.menu.application.exception;

import kitchenpos.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
