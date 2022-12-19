package kitchenpos.table.domain;

public class TableUngroupedEvent {

    private Long tableGroupId;

    public TableUngroupedEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
