package kitchenpos.order.event;

import kitchenpos.order.domain.OrderTable;

import java.util.List;

public class OrderTableGrouped {
    private Long tableGroupId;
    private List<OrderTable> orderTables;

    private OrderTableGrouped(Long tableGroupId, List<OrderTable> orderTables) {
        this.tableGroupId = tableGroupId;
        this.orderTables = orderTables;
    }

    public static OrderTableGrouped from(Long tableGroupId, List<OrderTable> orderTables) {
        return new OrderTableGrouped(tableGroupId, orderTables);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
