package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {
    private List<TableRequest> orderTables;

    public TableGroupRequest() {}

    public TableGroupRequest(List<TableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableRequest> getOrderTables() {
        return orderTables;
    }
}
