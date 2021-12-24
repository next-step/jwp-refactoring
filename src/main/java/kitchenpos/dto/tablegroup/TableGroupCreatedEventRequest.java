package kitchenpos.dto.tablegroup;

import java.util.List;

public class TableGroupCreatedEventRequest {

    private Long tableGroupId;
    private List<Long> orderTableIds;

    public TableGroupCreatedEventRequest(Long tableGroupId, List<Long> orderTableIds) {
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
