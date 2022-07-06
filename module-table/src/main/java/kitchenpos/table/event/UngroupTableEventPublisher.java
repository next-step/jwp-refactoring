package kitchenpos.table.event;

import kitchenpos.table.domain.TableGroup;

public class UngroupTableEventPublisher {
    private final TableGroup tableGroup;

    public UngroupTableEventPublisher(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup tableGroup() {
        return tableGroup;
    }
}
