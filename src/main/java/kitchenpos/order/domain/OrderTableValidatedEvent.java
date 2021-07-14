package kitchenpos.order.domain;

public class OrderTableValidatedEvent {
    private Long orderTableId;

    public OrderTableValidatedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
