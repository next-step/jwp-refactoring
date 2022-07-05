package kitchenpos.order.domain;

public class OrderTableEmptyValidateEvent {
    private final Order order;

    public OrderTableEmptyValidateEvent(Order order) {
        this.order = order;
    }

    public Long getOrderTableId() {
        return order.getOrderTableId();
    }

}
