package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;

public class TableChangeEmptyEventPublisher {

    private final OrderTable orderTable;

    public TableChangeEmptyEventPublisher(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
