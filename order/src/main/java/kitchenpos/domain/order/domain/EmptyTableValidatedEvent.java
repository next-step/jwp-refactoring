package kitchenpos.domain.order.domain;

public class EmptyTableValidatedEvent {

    private Long orderTableId;

    public EmptyTableValidatedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
