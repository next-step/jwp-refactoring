package kitchenpos.table.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class CannotChangeEmptyException extends BusinessException {
    public CannotChangeEmptyException() {
        super("테이블 상태를 변경할 수 없습니다.");
    }

    public CannotChangeEmptyException(String message) {
        super(message);
    }
}
