package kitchenpos.table.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class CannotChangeGuestEmptyTableException extends BusinessException {
    public CannotChangeGuestEmptyTableException() {
        super("빈테이블에 인원수를 변경할 수 없습니다.");
    }

    public CannotChangeGuestEmptyTableException(String message) {
        super(message);
    }
}
