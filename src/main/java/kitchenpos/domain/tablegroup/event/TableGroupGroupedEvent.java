package kitchenpos.domain.tablegroup.event;

import java.util.List;

import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupGroupedEvent {

    private TableGroup tableGroup;
    private List<Long> orderTableIds;

    public TableGroupGroupedEvent(TableGroup tableGroup, List<Long> orderTableIds) {
        this.tableGroup = tableGroup;
        this.orderTableIds = orderTableIds;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
