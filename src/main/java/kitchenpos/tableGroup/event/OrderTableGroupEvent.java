package kitchenpos.tableGroup.event;

import kitchenpos.order.domain.OrderTable;

import java.util.List;

public class OrderTableGroupEvent {
    private Long tableGroupId;
    private List<OrderTable> orderTables;

    private OrderTableGroupEvent(Long tableGroupId, List<OrderTable> orderTables) {
        this.tableGroupId = tableGroupId;
        this.orderTables = orderTables;
    }

    public static OrderTableGroupEvent from(Long tableGroupId, List<OrderTable> orderTables) {
        return new OrderTableGroupEvent(tableGroupId, orderTables);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
