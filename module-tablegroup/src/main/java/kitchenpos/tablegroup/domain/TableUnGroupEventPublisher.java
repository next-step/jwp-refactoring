package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.OrderTable;

import java.util.List;

public class TableUnGroupEventPublisher {
    private final List<OrderTable> orderTables;

    public TableUnGroupEventPublisher(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
