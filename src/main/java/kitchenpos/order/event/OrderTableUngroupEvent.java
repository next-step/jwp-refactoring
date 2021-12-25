package kitchenpos.order.event;

import kitchenpos.tableGroup.domain.TableGroup;

public class OrderTableUngroupEvent {
    private TableGroup tableGroup;

    private OrderTableUngroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public static OrderTableUngroupEvent from(TableGroup tableGroup){
        return new OrderTableUngroupEvent(tableGroup);
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
    public Long getTableGroupId(){
        return tableGroup.getId();
    }
}
