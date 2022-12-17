package kitchenpos.tablegroup.domain;

import java.util.List;

public class TableGroupedEvent {
    private final TableGroup tableGroup;
    private final List<Long> tableIds;

    public TableGroupedEvent(TableGroup tableGroup, List<Long> tableIds) {
        this.tableGroup = tableGroup;
        this.tableIds = tableIds;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
