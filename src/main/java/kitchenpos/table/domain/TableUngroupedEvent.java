package kitchenpos.table.domain;

import java.util.List;

public class TableUngroupedEvent {
    private final TableGroup tableGroup;

    public TableUngroupedEvent(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return tableGroup.getOrderTableIds();
    }
}
