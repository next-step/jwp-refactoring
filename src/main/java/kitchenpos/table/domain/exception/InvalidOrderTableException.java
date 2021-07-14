package kitchenpos.table.domain.exception;

import kitchenpos.common.error.exception.BusinessException;
public class InvalidOrderTableException extends BusinessException {
    public InvalidOrderTableException() {
        super("테이블 그룹의 검증과정에 실패하였습니다.");
    }

    public InvalidOrderTableException(String message) {
        super(message);
    }
}
