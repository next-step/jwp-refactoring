package domain.order.exception;

import common.error.BusinessException;

public class CannotChangeOrderStatusException extends BusinessException {
    public CannotChangeOrderStatusException() {
        super("계산완료된 주문 건에 대해서 주문 상태를 변경할 수 없습니다.");
    }

    public CannotChangeOrderStatusException(String message) {
        super(message);
    }
}
