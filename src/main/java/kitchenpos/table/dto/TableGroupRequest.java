package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public OrderTables toEntity() {
        return OrderTables.of(this.orderTables.stream()
                .map(orderTable -> OrderTable.of(orderTable.getId()))
                .collect(Collectors.toList()));
    }
}
