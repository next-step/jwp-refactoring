package kitchenpos.tablegroup.event;

public class TableUnGroupEventPublisher {
    private final Long tableGroupId;

    public TableUnGroupEventPublisher(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
