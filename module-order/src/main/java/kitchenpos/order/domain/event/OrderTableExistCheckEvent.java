package kitchenpos.order.domain.event;

public class OrderTableExistCheckEvent {

    private Long orderTableId;

    public OrderTableExistCheckEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }
}
