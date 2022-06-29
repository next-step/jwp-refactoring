package kitchenpos.table.dto;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    protected TableGroupRequest() {}

    private TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupRequest from(List<Long> orderTableIds) {
        return new TableGroupRequest(orderTableIds);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
