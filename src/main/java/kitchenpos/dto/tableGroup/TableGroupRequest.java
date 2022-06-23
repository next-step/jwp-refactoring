package kitchenpos.dto.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;

import java.util.List;

public class TableGroupRequest {
    private final List<OrderTable> orderTables;

    public TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
