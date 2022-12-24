package kitchenpos.table.dto;

import java.util.List;

public class CreateTableGroupRequest {

    private List<Long> orderTableIds;

    public CreateTableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTableIds;
    }
}
