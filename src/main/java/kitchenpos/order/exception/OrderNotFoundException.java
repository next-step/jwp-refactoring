package kitchenpos.order.exception;

import kitchenpos.common.exception.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "주문을 찾을 수 없습니다 : %d";

    public OrderNotFoundException(Long orderId) {
        super(String.format(DEFAULT_MESSAGE, orderId));
    }
}
