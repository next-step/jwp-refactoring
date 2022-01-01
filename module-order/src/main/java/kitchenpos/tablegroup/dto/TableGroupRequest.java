package kitchenpos.tablegroup.dto;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

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
        return new TableGroup(orderTables);
    }
}
