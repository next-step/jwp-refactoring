package kitchenpos.order.event;

public class ValidateEmptyTableEvent {
    private Long orderTableId;

    public ValidateEmptyTableEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
