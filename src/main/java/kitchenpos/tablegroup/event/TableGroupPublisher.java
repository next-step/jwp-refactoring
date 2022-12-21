package kitchenpos.tablegroup.event;

import kitchenpos.order.domain.OrderTable;

import java.util.List;

public class TableGroupPublisher {

    private final Long tableGroupId;
    private final List<OrderTable> orderTables;
    public TableGroupPublisher(Long tableGroupId, List<OrderTable> orderTables) {
        this.tableGroupId = tableGroupId;
        this.orderTables = orderTables;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
