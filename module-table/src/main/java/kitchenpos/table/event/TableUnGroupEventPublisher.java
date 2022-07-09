package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTables;

public class TableUnGroupEventPublisher {
    private final OrderTables orderTables;

    public TableUnGroupEventPublisher(OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
