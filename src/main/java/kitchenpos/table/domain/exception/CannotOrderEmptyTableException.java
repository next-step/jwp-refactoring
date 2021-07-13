package kitchenpos.table.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class CannotOrderEmptyTableException extends BusinessException {
    public CannotOrderEmptyTableException() {
        super("빈테이블에 주문할 수 없습니다.");
    }

    public CannotOrderEmptyTableException(String message) {
        super(message);
    }
}
