package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
