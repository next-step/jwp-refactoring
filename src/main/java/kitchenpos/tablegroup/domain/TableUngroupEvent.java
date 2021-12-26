package kitchenpos.tablegroup.domain;

public class TableUngroupEvent {

    private final Long tableGroupId;

    public TableUngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
