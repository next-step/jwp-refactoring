package kitchenpos.tablegroup.event;

import java.util.List;

public class GroupByEvent {
    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    private GroupByEvent(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public static GroupByEvent of(Long tableGroupId, List<Long> orderTableIds) {
        return new GroupByEvent(tableGroupId, orderTableIds);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
