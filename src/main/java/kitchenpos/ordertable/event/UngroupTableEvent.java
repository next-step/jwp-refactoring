package kitchenpos.ordertable.event;

public class UngroupTableEvent {
    private final Long tableGroupId;

    private UngroupTableEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public static UngroupTableEvent from(Long tableGroupId) {
        return new UngroupTableEvent(tableGroupId);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
