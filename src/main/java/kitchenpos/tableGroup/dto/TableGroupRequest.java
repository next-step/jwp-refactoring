package kitchenpos.tableGroup.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTable> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(new OrderTables(orderTables));
    }
}
