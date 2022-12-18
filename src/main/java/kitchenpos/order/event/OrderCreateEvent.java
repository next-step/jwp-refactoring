package kitchenpos.order.event;

public class OrderCreateEvent {

    private final Long orderTableId;

    public OrderCreateEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
