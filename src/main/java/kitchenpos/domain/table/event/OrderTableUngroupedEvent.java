package kitchenpos.domain.table.event;

import kitchenpos.domain.tablegroup.TableGroup;

public class OrderTableUngroupedEvent {

    private TableGroup tableGroup;

    public OrderTableUngroupedEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
