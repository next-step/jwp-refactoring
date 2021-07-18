package kitchenpos.tablegroup.event;

import kitchenpos.tablegroup.domain.TableGroup;

public class TableUnGroupEvent {
    private final TableGroup tableGroup;

    public TableUnGroupEvent(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
