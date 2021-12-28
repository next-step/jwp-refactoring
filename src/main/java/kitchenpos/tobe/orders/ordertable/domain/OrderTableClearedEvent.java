package kitchenpos.tobe.orders.ordertable.domain;

public class OrderTableClearedEvent {

    private final Object source;

    private final Long orderTableId;

    public OrderTableClearedEvent(final Object source, final Long orderTableId) {
        this.source = source;
        this.orderTableId = orderTableId;
    }

    public Object getSource() {
        return source;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
