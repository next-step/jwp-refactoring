package kitchenpos.table.domain;

import java.util.List;

public class TableGroupedEvent {

    private List<Long> orderTableIds;
    private TableGroup tableGroup;


    public TableGroupedEvent(List<Long> orderTableIds, TableGroup tableGroup) {
        this.orderTableIds = orderTableIds;
        this.tableGroup = tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
