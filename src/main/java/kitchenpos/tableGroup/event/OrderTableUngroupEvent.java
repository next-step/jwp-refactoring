package kitchenpos.tableGroup.event;

import kitchenpos.tableGroup.domain.TableGroup;

public class OrderTableUngroupEvent {
    private TableGroup tableGroup;

    public OrderTableUngroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public static OrderTableUngroupEvent from(TableGroup tableGroup){
        return new OrderTableUngroupEvent(tableGroup);
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
