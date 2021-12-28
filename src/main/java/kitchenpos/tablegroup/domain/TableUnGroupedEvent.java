package kitchenpos.tablegroup.domain;

public class TableUnGroupedEvent {

    private final Long tableGroupId;

    private TableUnGroupedEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public static TableUnGroupedEvent of(TableGroup tableGroup) {
        return new TableUnGroupedEvent(tableGroup.getId());
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
