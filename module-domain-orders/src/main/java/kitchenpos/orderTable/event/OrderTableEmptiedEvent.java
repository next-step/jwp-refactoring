package kitchenpos.orderTable.event;

import kitchenpos.orderTable.domain.OrderTable;

public class OrderTableEmptiedEvent {
    private final OrderTable orderTable;

    public OrderTableEmptiedEvent(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
