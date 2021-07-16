package domain.order.exception;

import common.error.BusinessException;

public class UngroupTableException extends BusinessException {
    public UngroupTableException() {
        super("테이블 그룹을 지을 수 없습니다.");
    }

    public UngroupTableException(String message) {
        super(message);
    }
}
