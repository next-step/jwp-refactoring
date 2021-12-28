package kitchenpos.order.application;

public class OrderTableValidateEvent {

    private final Long orderTableId;

    public OrderTableValidateEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
