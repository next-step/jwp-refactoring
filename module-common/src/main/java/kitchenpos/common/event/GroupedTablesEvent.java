package kitchenpos.common.event;

import java.util.List;

public class GroupedTablesEvent {
    private final List<Long> orderTableIds;
    private final Long tableGroupId;

    public GroupedTablesEvent(List<Long> orderTableIds, Long tableGroupId) {
        this.orderTableIds = orderTableIds;
        this.tableGroupId = tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
