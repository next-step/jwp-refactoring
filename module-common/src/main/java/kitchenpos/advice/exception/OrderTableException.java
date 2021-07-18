package kitchenpos.advice.exception;

import java.util.List;

public class OrderTableException extends BusinessException {

    public OrderTableException(String message) {
        super(message);
    }

    public OrderTableException(String message, List<String> orderStatuses) {
        super(message + "주문상태 : " + orderStatuses);
    }

    public OrderTableException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
