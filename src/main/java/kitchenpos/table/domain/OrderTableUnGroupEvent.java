package kitchenpos.table.domain;

public class OrderTableUnGroupEvent {
    private final TableGroup tableGroup;

    public OrderTableUnGroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
