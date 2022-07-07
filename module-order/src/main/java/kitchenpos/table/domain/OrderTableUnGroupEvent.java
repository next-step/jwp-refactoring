package kitchenpos.table.domain;

import kitchenpos.tableGroup.domain.TableGroup;

public class OrderTableUnGroupEvent {
    private final TableGroup tableGroup;

    public OrderTableUnGroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
