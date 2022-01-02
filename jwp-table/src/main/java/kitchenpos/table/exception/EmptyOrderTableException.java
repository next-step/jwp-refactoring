package kitchenpos.table.exception;

import kitchenpos.order.exception.CustomException;

public class EmptyOrderTableException extends CustomException {
    public static final String EMPTY_ORDER_TABLE_MESSAGE = "주문 테이블은 필수로 존재해야 합니다.";

    public EmptyOrderTableException() {
        super(EMPTY_ORDER_TABLE_MESSAGE);
    }
}
