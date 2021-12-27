package kitchenpos.tablegroup.domain;

public class TableUnGroupingEvent {

    private final Long tableGroupId;

    public TableUnGroupingEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
