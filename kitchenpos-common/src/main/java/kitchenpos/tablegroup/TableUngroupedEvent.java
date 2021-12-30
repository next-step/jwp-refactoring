package kitchenpos.tablegroup;

import kitchenpos.table.OrderTables;

public class TableUngroupedEvent {

    private OrderTables orderTables;

    public TableUngroupedEvent(OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
