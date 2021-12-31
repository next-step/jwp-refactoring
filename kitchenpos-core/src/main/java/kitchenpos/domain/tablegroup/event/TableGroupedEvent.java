package kitchenpos.domain.tablegroup.event;

import kitchenpos.domain.tablegroup.domain.TableGroup;

import java.util.List;

public class TableGroupedEvent {

    private final List<Long> orderTableIds;
    private final Long tableGroupId;

    private TableGroupedEvent(List<Long> orderTableIds, Long tableGroupId) {
        this.orderTableIds = orderTableIds;
        this.tableGroupId = tableGroupId;
    }

    public static TableGroupedEvent of(TableGroup tableGroup) {
        return new TableGroupedEvent(tableGroup.getOrderTableIds(), tableGroup.getId());
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
