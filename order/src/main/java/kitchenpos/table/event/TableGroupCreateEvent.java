package kitchenpos.table.event;

import java.util.List;

public class TableGroupCreateEvent {

    private List<Long> orderTableIds;
    private Long tableGroupId;

    public TableGroupCreateEvent(List<Long> orderTableIds, Long tableGroupId) {
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
