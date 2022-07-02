package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dto.OrderTableIdRequest;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }


    public List<Long> toOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

}
