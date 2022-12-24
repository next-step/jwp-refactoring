package kitchenpos.table.event;

public class TableUnGroupEvent {

    private final Long tableGroupId;

    public TableUnGroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
