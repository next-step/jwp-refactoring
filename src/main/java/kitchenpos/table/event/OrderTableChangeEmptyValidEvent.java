package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;

public class OrderTableChangeEmptyValidEvent {

    private OrderTable orderTable;
    private boolean empty;

    public OrderTableChangeEmptyValidEvent(OrderTable orderTable, boolean empty) {
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
