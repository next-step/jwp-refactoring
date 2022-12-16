package kitchenpos.tablegroup.event;

public class UnGroupEvent {

    private Long tableGroupId;

    public UnGroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
