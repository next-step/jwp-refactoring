package kitchenpos.table.exception;

import kitchenpos.order.exception.CustomException;

public class EmptyOrderException extends CustomException {
    public static final String EMPTY_ORDER_MESSAGE = "주문 정보가 존재하지 않습니다.";

    public EmptyOrderException() {
        super(EMPTY_ORDER_MESSAGE);
    }
}
