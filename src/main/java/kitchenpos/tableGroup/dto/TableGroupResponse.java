package kitchenpos.tableGroup.dto;

import kitchenpos.tableGroup.domain.TableGroup;

import java.util.List;

public class TableGroupResponse {

    private Long tableGroupId;
    private List<Long> orderTableIds;

    public TableGroupResponse() {
    }

    public TableGroupResponse(TableGroup tableGroup) {
        this.tableGroupId = tableGroup.getId();
        this.orderTableIds = tableGroup.getOrderTableIds();
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
