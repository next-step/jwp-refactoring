package kitchenpos.domain;

public class OrderCreatedEvent {
    private final Long orderTableId;

    public OrderCreatedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
