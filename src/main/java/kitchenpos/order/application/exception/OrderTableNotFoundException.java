package kitchenpos.order.application.exception;

import kitchenpos.common.NotFoundException;

public class OrderTableNotFoundException extends NotFoundException {
    public OrderTableNotFoundException() {
        super("주문 테이블을 찾을 수 없습니다.");
    }

    public OrderTableNotFoundException(String message) {
        super(message);
    }
}
