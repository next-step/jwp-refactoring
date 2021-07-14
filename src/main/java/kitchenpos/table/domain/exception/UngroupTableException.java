package kitchenpos.table.domain.exception;

import kitchenpos.common.error.exception.BusinessException;
public class UngroupTableException extends BusinessException {
    public UngroupTableException() {
        super("테이블 그룹을 지을 수 없습니다.");
    }

    public UngroupTableException(String message) {
        super(message);
    }
}
