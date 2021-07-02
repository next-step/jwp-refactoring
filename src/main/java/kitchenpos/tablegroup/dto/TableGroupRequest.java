package kitchenpos.tablegroup.dto;

import kitchenpos.ordertable.dto.OrderTableRequest;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}