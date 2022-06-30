package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupRequest from(List<Long> ids) {
        return new TableGroupRequest(ids);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
