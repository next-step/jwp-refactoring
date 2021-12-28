package kitchenpos.order.exception;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusException extends IllegalArgumentException {

    private static final String MESSAGE = "주문 상태가 %s 또는 %s입니다.";

    public OrderStatusException() {
        super(String.format(MESSAGE, OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}
