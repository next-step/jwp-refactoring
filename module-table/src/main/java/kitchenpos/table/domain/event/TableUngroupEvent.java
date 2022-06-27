package kitchenpos.table.domain.event;

import kitchenpos.table.domain.TableGroup;

public class TableUngroupEvent {

    private TableGroup tableGroup;

    public TableUngroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }
}
