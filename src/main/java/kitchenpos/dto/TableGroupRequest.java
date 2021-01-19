package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

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
