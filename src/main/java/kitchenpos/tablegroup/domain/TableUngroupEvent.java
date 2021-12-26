package kitchenpos.tablegroup.domain;

public class TableUngroupEvent {

    private final TableGroup tableGroup;

    public TableUngroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
