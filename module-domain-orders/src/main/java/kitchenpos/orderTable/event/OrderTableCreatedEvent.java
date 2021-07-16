package kitchenpos.orderTable.event;

public class OrderTableCreatedEvent {
    private final Long orderTableId;

    public OrderTableCreatedEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
