package kitchenpos.advice.exception;

import kitchenpos.order.domain.OrderStatus;

import java.util.List;

public class OrderTableException extends RuntimeException {

    public OrderTableException(String message) {
        super(message);
    }

    public OrderTableException(String message, List<OrderStatus> orderStatuses) {
        super(message + "주문상태 : " + orderStatuses);
    }

    public OrderTableException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
