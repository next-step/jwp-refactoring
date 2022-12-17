package kitchenpos.order.domain;

public class OrderCompletionEvent {
    private final Long orderTableId;
    public OrderCompletionEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long orderTableId() {
        return orderTableId;
    }
}
