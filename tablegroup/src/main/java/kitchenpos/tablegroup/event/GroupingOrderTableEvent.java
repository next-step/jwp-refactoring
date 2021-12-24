package kitchenpos.tablegroup.event;

import java.util.List;

public class GroupingOrderTableEvent {
    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public GroupingOrderTableEvent(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTableIds;
    }
}
