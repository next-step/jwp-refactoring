package kitchenpos.tablegroup.domain;

import java.util.List;

public class TableGroupSaveEvent {

    private Long tableGroupId;
    private List<Long> orderTableId;

    public TableGroupSaveEvent(final Long tableGroupId, final List<Long> orderTableId) {
        this.tableGroupId = tableGroupId;
        this.orderTableId = orderTableId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableId() {
        return orderTableId;
    }
}
