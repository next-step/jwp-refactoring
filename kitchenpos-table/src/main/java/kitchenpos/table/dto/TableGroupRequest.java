package kitchenpos.table.dto;

import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTables;

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables;
    }
}
