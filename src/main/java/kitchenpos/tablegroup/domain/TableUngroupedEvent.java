package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTables;

public class TableUngroupedEvent {

    private OrderTables orderTables;

    public TableUngroupedEvent(OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
