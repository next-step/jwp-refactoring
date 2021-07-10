package kitchenpos.order.event;

import java.util.List;

import kitchenpos.tablegroup.domain.TableGroup;

public class GroupCreatedEvent {
    private final List<Long> orderTableIds;
    private final TableGroup tableGroup;

    public GroupCreatedEvent(List<Long> orderTableIds, TableGroup tableGroup) {
        this.orderTableIds = orderTableIds;
        this.tableGroup = tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
