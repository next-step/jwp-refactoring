package kitchenpos.order.domain;

public class OrderCreatedEvent {

    private Long orderTableId;

    public OrderCreatedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
