package kitchenpos.tablegroup.domain;

public class TableUnGroupedEvent {
    private final Long tableGroupId;

    public TableUnGroupedEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
