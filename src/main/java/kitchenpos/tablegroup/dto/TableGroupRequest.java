package kitchenpos.tablegroup.dto;

import java.util.ArrayList;
import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds = new ArrayList<>();

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
