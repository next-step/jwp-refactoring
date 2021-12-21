package kitchenpos.tableGroup.dto;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderTableRequest;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest() {

    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
