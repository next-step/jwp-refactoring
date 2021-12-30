package kitchenpos.tablegroup.domain;

public class UngroupingTableEvent {
    private final Long tableGroupId;

    public UngroupingTableEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
