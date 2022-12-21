package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupCreatedEvent {
    private final List<Long> orderTableIds;
    private final Long tableGroupId;

    public TableGroupCreatedEvent(List<Long> orderTableIds, Long tableGroupId) {
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
