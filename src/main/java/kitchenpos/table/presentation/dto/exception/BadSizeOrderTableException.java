package kitchenpos.table.presentation.dto.exception;

import kitchenpos.common.error.exception.BusinessException;

public class BadSizeOrderTableException extends BusinessException {
    public BadSizeOrderTableException() {
        super("주문 테이블은 최소 2개이상이어야 합니다.");
    }

    public BadSizeOrderTableException(String message) {
        super(message);
    }
}
