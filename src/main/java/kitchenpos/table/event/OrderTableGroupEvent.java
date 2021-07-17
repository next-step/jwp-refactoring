package kitchenpos.table.event;

import kitchenpos.table.domain.TableGroup;

import java.util.List;

public class OrderTableGroupEvent {

    private TableGroup tableGroup;
    private List<Long> orderTableIds;

    public OrderTableGroupEvent(TableGroup tableGroup, List<Long> orderTableIds) {
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
