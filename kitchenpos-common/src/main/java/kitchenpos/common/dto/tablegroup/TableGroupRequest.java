package kitchenpos.common.dto.tablegroup;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    protected TableGroupRequest() {}

    private TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(List<OrderTableIdRequest> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
