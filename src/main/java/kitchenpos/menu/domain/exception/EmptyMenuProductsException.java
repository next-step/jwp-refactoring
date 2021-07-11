package kitchenpos.menu.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class EmptyMenuProductsException extends BusinessException {
    public EmptyMenuProductsException() {
        super("메뉴 상품은 최소 한개 이상이어야 합니다.");
    }

    public EmptyMenuProductsException(String message) {
        super(message);
    }
}
