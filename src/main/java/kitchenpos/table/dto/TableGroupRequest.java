package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    protected TableGroupRequest() {}

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupRequest of(List<Long> orderTables) {
        //return new TableGroupRequest(createOrderTableIds(orderTables));
        return new TableGroupRequest(orderTables);
    }

    public static TableGroupRequest create(List<Long> orderTableIds) {
        return new TableGroupRequest(orderTableIds);
    }

    private static List<Long> createOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> orderTable.getId())
                .collect(Collectors.toList());
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
