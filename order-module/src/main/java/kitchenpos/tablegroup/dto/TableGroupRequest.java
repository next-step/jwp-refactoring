package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> ids) {
        orderTables = ids.stream()
                .map(OrderTableRequest::new)
                .collect(Collectors.toList());
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(orderTable -> orderTable.getId())
                .collect(Collectors.toList());
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
