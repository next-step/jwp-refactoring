package kitchenpos.domain.tablegroup.event;

import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupUngroupedEvent {

    private TableGroup tableGroup;

    public TableGroupUngroupedEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
