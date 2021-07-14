package kitchenpos.dto.tablegroup;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTables;

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}