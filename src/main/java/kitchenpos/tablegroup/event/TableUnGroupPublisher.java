package kitchenpos.tablegroup.event;

public class TableUnGroupPublisher {

    private final Long tableGroupId;

    public TableUnGroupPublisher(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
