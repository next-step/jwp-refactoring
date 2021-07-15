package kitchenpos.order.event;

public class OrderStatusChangedEvent {
    private Long orderId;

    public OrderStatusChangedEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
