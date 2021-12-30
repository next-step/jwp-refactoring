package kitchenpos.domain.tablegroup;

import kitchenpos.domain.table.OrderTables;

public class TableUngroupedEvent {

    private OrderTables orderTables;

    public TableUngroupedEvent(OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
