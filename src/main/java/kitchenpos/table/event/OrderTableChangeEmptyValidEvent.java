package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;

public class OrderTableChangeEmptyValidEvent {

    private OrderTable orderTable;

    public OrderTableChangeEmptyValidEvent(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
