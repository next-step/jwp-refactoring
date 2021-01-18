package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTable> orderTables;

    public TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
