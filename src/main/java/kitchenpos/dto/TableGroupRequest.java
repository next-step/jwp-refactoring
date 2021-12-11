package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<TableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<TableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(TableRequest::getId)
                .collect(Collectors.toList());
    }
}
