package kitchenpos.advice.exception;

import kitchenpos.order.domain.OrderStatus;

public class OrderException extends RuntimeException {

    public OrderException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }

    public OrderException(String message, OrderStatus orderStatus) {
        super(String.format(message + " %s ", orderStatus.name()));
    }
}
