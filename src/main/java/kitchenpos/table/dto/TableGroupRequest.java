package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public List<Long> toOrderTableId() {
        return orderTables.stream()
                          .map(OrderTableIdRequest::getId)
                          .collect(Collectors.toList());
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
