package kitchenpos.tablegroup.domain;

public class TableUngroupedEvent {
    private final TableGroup tableGroup;

    public TableUngroupedEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
