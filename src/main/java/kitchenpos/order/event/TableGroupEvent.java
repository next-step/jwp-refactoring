package kitchenpos.order.event;

import java.util.List;

public class TableGroupEvent {

    Long tableGroupId;
    List<Long> orderTableIds;

    public TableGroupEvent(Long tableGroupId, List<Long> orderTableIds) {
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
