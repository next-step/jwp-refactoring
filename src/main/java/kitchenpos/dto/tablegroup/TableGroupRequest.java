package kitchenpos.dto.tablegroup;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    protected TableGroupRequest() {
    }

    private TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupRequest from(final List<Long> orderTableIds) {
        return new TableGroupRequest(orderTableIds);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
