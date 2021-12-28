package kitchenpos.table.domain;

import java.util.List;

public class TableGroupEvent {
    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    private TableGroupEvent(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupEvent of(Long tableId, List<Long> orderTableIds) {
        return new TableGroupEvent(tableId, orderTableIds);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
