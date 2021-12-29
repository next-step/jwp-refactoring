package kitchenpos.tablegroup.domain;

import java.util.Collections;
import java.util.List;

public class GroupingTableEvent {
    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public GroupingTableEvent(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return Collections.unmodifiableList(orderTableIds);
    }

    public int getTablesSize() {
        return orderTableIds.size();
    }
}
