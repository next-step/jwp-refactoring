package kitchenpos.tablegroup.event;

import java.util.List;

public class GroupInfo {

    private Long tableGroupId;
    private List<Long> orderTableIds;

    public GroupInfo(Long tableGroupId, List<Long> orderTableIds) {
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
