package kitchenpos.tablegroup.event;

public class UngroupEvent {
    private final Long tableGroupId;

    private UngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public static UngroupEvent from(Long tableGroupId) {
        return new UngroupEvent(tableGroupId);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
