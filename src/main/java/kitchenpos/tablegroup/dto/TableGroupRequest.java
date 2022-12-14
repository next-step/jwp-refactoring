package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.dto.OrderTableIdRequest;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    private TableGroupRequest() {
    }

    private TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(List<Long> orderTableIds) {
        return new TableGroupRequest(
                orderTableIds.stream()
                        .map(OrderTableIdRequest::from)
                        .collect(Collectors.toList())
        );
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
