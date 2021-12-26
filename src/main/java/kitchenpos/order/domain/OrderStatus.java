package kitchenpos.order.domain;

import java.util.function.Function;

public enum OrderStatus {
    COMPLETION(null),
    MEAL(COMPLETION),
    COOKING(MEAL);

    private final OrderStatus nextOrderStatus;

    OrderStatus(OrderStatus nextOrderStatus) {
        this.nextOrderStatus = nextOrderStatus;
    }

    public OrderStatus next() {
        return this.nextOrderStatus;
    }
}
