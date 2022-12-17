package kitchenpos.table.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    private TableGroupRequest() {}

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        if (orderTables != null) {
            return orderTables.stream()
                    .map(OrderTableIdRequest::getId)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
