package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup toTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = TableGroup.builder().build();
        tableGroup.addOrderTables(orderTables);
        return tableGroup;
    }
}
