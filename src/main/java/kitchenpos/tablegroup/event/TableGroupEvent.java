package kitchenpos.tablegroup.event;

import kitchenpos.table.domain.OrderTable;

import java.util.List;

public class TableGroupEvent {

    private final Long tableGroupId;
    private final List<OrderTable> orderTables;
    public TableGroupEvent(Long tableGroupId, List<OrderTable> orderTables) {
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
