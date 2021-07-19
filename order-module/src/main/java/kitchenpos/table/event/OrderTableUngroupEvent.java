package kitchenpos.table.event;

import kitchenpos.table.domain.TableGroup;

public class OrderTableUngroupEvent {

    private TableGroup tableGroup;

    public OrderTableUngroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

}
