package kitchenpos.ordertable.event;

import java.util.List;

public class ReserveEvent {
    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public ReserveEvent(Long tableGroupId, List<Long> orderTableIds) {
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
