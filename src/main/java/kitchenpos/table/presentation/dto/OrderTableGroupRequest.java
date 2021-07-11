package kitchenpos.table.presentation.dto;

import java.util.List;

public class OrderTableGroupRequest {
    private List<OrderTableRequest> orderTables;

    protected OrderTableGroupRequest() {
    }

    public OrderTableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTableGroupRequest of(List<OrderTableRequest> orderTables) {
        return new OrderTableGroupRequest(orderTables);
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
