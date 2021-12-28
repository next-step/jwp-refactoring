package kitchenpos.table.domain.event;

import java.util.List;

public class GroupTable {

    private Long tableGroupId;
    private List<Long> orderTableIds;

    public GroupTable(Long tableGroupId,
        List<Long> orderTableIds) {
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
