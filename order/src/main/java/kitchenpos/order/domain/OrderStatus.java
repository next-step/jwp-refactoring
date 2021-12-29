package kitchenpos.order.domain;

public enum OrderStatus {
    COMPLETION(null),
    MEAL(COMPLETION),
    COOKING(MEAL);

    private final OrderStatus nextOrderStatus;

    OrderStatus(OrderStatus nextOrderStatus) {
        this.nextOrderStatus = nextOrderStatus;
    }

    public OrderStatus next() {
        if(this == OrderStatus.COMPLETION) {
            return this;
        }
        return this.nextOrderStatus;
    }
}
