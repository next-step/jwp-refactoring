package kitchenpos.table.application.exception;

import kitchenpos.common.error.exception.BusinessException;

public class BadSizeOrderTableException extends BusinessException {
    public BadSizeOrderTableException(int minValue) {
        super(String.format("그룹화시 주문 테이블은 최소 %d개 이상이어야 합니다.", minValue));
    }

    public BadSizeOrderTableException(String message) {
        super(message);
    }
}
