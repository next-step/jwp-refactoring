package kitchenpos.order.domain;

import kitchenpos.table.domain.TableStatus;

public class OrderStatusEvent {
    private Long orderTableId;
    private OrderStatus orderStatus;

    public OrderStatusEvent(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public TableStatus getStatus() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            return TableStatus.EMPTY;
        }
        return TableStatus.ORDERED;
    }
}
