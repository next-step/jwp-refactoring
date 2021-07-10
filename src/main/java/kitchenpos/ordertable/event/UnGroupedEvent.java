package kitchenpos.ordertable.event;

import kitchenpos.tablegroup.domain.TableGroup;

public class UnGroupedEvent {
    private final TableGroup tableGroup;

    public UnGroupedEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
