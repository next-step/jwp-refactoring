package api.order.dto;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
