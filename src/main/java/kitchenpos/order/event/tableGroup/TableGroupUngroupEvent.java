package kitchenpos.order.event.tableGroup;

public class TableGroupUngroupEvent {
    private Long tableGroupId;

    public TableGroupUngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
