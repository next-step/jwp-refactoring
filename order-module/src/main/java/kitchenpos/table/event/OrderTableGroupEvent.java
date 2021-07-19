package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTables;

import java.util.List;

public class OrderTableGroupEvent {

    private List<Long> orderTableIds;
    private OrderTables orderTables;

    public OrderTableGroupEvent(List<Long> orderTableIds, OrderTables orderTables) {
        this.orderTableIds = orderTableIds;
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
