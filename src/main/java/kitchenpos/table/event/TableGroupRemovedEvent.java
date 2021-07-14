package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTables;

public class TableGroupRemovedEvent  {
    private final OrderTables orderTables;

    public TableGroupRemovedEvent(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}

