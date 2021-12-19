package kitchenpos.common.domain.tablegroup.event;

import java.util.List;

public class TableGroupGroupedEvent {

    private Long tableGroupId;
    private List<Long> orderTableIds;

    public TableGroupGroupedEvent(Long tableGroupId, List<Long> orderTableIds) {
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
