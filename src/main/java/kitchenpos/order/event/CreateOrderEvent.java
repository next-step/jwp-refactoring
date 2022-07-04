package kitchenpos.order.event;

public class CreateOrderEvent {
    private final Long orderTableId;

    private CreateOrderEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public static CreateOrderEvent from(Long orderTableId) {
        return new CreateOrderEvent(orderTableId);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
