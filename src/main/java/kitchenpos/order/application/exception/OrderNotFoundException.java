package kitchenpos.order.application.exception;

import kitchenpos.common.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
