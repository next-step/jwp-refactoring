package kitchenpos.tablegroup.dto;

import java.util.List;

public class CreateTableGroupRequest {

    private List<Long> orderTableIds;

    public CreateTableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    protected CreateTableGroupRequest() {
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
