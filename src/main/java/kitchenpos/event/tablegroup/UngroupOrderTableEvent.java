package kitchenpos.event.tablegroup;

public class UngroupOrderTableEvent {
    private Long tableGroupId;

    public UngroupOrderTableEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
