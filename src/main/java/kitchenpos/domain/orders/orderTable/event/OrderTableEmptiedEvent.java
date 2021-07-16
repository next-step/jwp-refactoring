package kitchenpos.domain.orders.orderTable.event;

import kitchenpos.domain.orders.orderTable.domain.OrderTable;

public class OrderTableEmptiedEvent {
    private final OrderTable orderTable;

    public OrderTableEmptiedEvent(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
