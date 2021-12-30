package kitchenpos.table.domain;

import java.util.List;

public class TableGroupEvent {
    private final Long tableGroupId;
    private final List<OrderTable> orderTables;

    private TableGroupEvent(Long tableGroupId, List<OrderTable> orderTables) {
        this.tableGroupId = tableGroupId;
        this.orderTables = orderTables;
    }

    public static TableGroupEvent of(Long tableId, List<OrderTable> orderTables) {
        return new TableGroupEvent(tableId, orderTables);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
