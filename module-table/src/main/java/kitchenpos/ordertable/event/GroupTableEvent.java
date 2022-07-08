package kitchenpos.ordertable.event;

import java.util.List;

public class GroupTableEvent {
    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    private GroupTableEvent(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public static GroupTableEvent of(Long tableGroupId, List<Long> orderTableIds) {
        return new GroupTableEvent(tableGroupId, orderTableIds);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
