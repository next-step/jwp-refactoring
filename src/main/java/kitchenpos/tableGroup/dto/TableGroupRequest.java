package kitchenpos.tableGroup.dto;

import kitchenpos.order.dto.OrderRequest;

import java.util.List;

public class TableGroupRequest {
    private List<OrderRequest> orderTables;

    public TableGroupRequest(List<OrderRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
