package kitchenpos.table.dto;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupRequest {
    List<TableGroupOrderTableRequest> orderTables;

    public List<TableGroupOrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<TableGroupOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(TableGroupOrderTableRequest::getId)
                .collect(toList());
    }
}
