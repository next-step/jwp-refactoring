package kitchenpos.table.event;

public class TableUnGroupEvent {

    private Long tableGroupId;

    public TableUnGroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
