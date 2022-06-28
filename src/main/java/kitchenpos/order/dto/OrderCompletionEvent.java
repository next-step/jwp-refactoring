package kitchenpos.order.dto;

public class OrderCompletionEvent {

    private final Long orderTableId;

    public OrderCompletionEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
