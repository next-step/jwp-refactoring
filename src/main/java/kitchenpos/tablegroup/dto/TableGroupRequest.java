package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {
    public List<Long> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
