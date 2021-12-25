package kitchenpos.order.application.exception;

import kitchenpos.common.NotFoundException;

public class OrderLineItemNotFoundException extends NotFoundException {
    public OrderLineItemNotFoundException() {
        super("주문 항목을 찾을 수 없습니다.");
    }

    public OrderLineItemNotFoundException(String message) {
        super(message);
    }
}
