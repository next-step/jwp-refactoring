package kitchenpos.table.dto;

import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTableIds;

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    protected TableGroupRequest() {
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

}
