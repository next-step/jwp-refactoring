package kitchenpos.order.domain;

public class OrderTableValidateEvent {
    private final Long orderTableId;

    private OrderTableValidateEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public static OrderTableValidateEvent of(Long orderTableId) {
        return new OrderTableValidateEvent(orderTableId);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
