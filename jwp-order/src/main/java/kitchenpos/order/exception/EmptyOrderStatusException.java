package kitchenpos.order.exception;

import kitchenpos.menu.exception.CustomException;

public class EmptyOrderStatusException extends CustomException {
    public static final String EMPTY_ORDER_TABLE_STATUS_MESSAGE = "주문 상태는 필수로 입력되어야 합니다.";

    public EmptyOrderStatusException() {
        super(EMPTY_ORDER_TABLE_STATUS_MESSAGE);
    }
}
