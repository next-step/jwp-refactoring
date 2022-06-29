package kitchenpos.order.domain.event;

public class OrderTableEmptyCheckEvent {

    private Long orderTableId;

    public OrderTableEmptyCheckEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }
}
