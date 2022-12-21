package kitchenpos.tablegroup.event;

public class TableUnGroupedEvent {

    private Long tableGroupId;

    public TableUnGroupedEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
