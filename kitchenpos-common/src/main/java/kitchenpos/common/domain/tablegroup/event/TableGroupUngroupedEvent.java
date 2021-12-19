package kitchenpos.common.domain.tablegroup.event;

public class TableGroupUngroupedEvent {

    private Long tableGroupId;

    public TableGroupUngroupedEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
