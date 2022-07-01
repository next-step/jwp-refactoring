package kitchenpos.table.dto;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

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
        return new TableGroup(orderTables);
    }
}
