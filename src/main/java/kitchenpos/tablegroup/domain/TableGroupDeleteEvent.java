package kitchenpos.tablegroup.domain;

public class TableGroupDeleteEvent {

    private Long tableGroupId;

    public TableGroupDeleteEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
