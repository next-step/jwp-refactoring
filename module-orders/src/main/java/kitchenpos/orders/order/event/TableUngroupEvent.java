package kitchenpos.orders.order.event;

public class TableUngroupEvent {

        private final Long tableGroupId;

    public TableUngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
