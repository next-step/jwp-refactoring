package kitchenpos.dto;

import kitchenpos.domain.OrderTables;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
