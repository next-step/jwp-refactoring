package kitchenpos.table.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class UnUseOrderTableException extends BusinessException {
    public UnUseOrderTableException() {
        super("빈테이블에 주문할 수 없습니다.");
    }

    public UnUseOrderTableException(String message) {
        super(message);
    }
}
