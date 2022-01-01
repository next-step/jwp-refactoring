package kitchenpos.tableGroup.event;

import java.util.List;

public class GroupEvent {
    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public GroupEvent(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
