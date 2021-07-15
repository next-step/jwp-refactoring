package kitchenpos.advice.exception;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;

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
