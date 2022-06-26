package kitchenpos.dto.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.tableGroup.TableGroup;

import java.util.List;

public class TableGroupRequest {
    private final List<OrderTable> orderTables;

    public TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(null,  orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
