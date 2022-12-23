package kitchenpos.table.dto;

import java.util.ArrayList;
import java.util.List;

public class TableGroupRequest {
    private List<TableRequest> orderTables = new ArrayList<>();

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<TableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableRequest> getOrderTables() {
        return orderTables;
    }
}
