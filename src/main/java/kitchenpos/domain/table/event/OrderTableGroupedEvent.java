package kitchenpos.domain.table.event;

import java.util.List;

import kitchenpos.domain.tablegroup.TableGroup;

public class OrderTableGroupedEvent {

    private TableGroup tableGroup;
    private List<Long> orderTableIds;

    public OrderTableGroupedEvent(TableGroup tableGroup, List<Long> orderTableIds) {
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
