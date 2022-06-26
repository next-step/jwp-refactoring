package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {
    List<Long> orderTables;

    protected TableGroupRequest() {
    }

    private TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }
}
