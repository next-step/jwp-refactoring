package domain.order.exception;

import common.error.BusinessException;

public class CannotChangeEmptyException extends BusinessException {
    public CannotChangeEmptyException() {
        super("테이블 상태를 변경할 수 없습니다.");
    }

    public CannotChangeEmptyException(String message) {
        super(message);
    }
}
