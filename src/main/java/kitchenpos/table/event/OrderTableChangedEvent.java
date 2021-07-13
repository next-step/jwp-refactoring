package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;

public class OrderTableChangedEvent {
    private final OrderTable orderTable;
    private final boolean empty;

    public OrderTableChangedEvent(final OrderTable orderTable, final boolean empty) {
        this.orderTable = orderTable;
        this.empty = empty;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public boolean isEmpty() {
        return empty;
    }
}
