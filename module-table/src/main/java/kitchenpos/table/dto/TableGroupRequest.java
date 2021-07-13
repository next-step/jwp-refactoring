package kitchenpos.table.dto;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    private TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupRequest of(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

}
