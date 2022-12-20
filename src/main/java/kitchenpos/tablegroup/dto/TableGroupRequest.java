package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dto.OrderTableRequest;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
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
