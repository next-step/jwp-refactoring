package kitchenpos.order.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class CannotUngroupException extends BusinessException {
    public CannotUngroupException() {
        super("주문 상태가 완료인 경우에만 테이블 그룹을 해제할 수 있습니다.");
    }

    public CannotUngroupException(String message) {
        super(message);
    }
}
