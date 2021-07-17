package kitchenpos.tablegroup.domain;

import java.util.List;

public class TableGroupedEvent {
    private final TableGroup tableGroup;
    private final List<Long> orderTableIds;

    public TableGroupedEvent(TableGroup tableGroup, List<Long> orderTableIds) {
        this.tableGroup = tableGroup;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
