package kitchenpos.domain.orders.orderTable.event;

import kitchenpos.domain.orders.orderTable.domain.OrderTables;

public class TableGroupRemovedEvent  {
    private final OrderTables orderTables;

    public TableGroupRemovedEvent(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}

