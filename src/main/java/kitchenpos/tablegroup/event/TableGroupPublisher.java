package kitchenpos.tablegroup.event;

public class TableGroupPublisher {

    private final Long tableGroupId;

    public TableGroupPublisher(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
