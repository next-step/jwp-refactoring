package kitchenpos.domain.tablegroup.event;

import kitchenpos.domain.tablegroup.TableGroup;

public class TableUngroupEvent {

    private TableGroup tableGroup;

    public TableUngroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }
}
