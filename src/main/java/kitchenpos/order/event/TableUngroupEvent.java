package kitchenpos.order.event;

public class TableUngroupEvent {

    Long tableGroupId;

    public TableUngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}