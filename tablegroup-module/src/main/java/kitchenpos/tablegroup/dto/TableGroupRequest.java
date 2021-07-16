package kitchenpos.tablegroup.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableGroupRequest {
    private List<OrderTable> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = Collections.unmodifiableList(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }
}
