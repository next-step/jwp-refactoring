package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupRequest of(List<OrderTableIdRequest> orderTableIds) {
        return new TableGroupRequest(orderTableIds);
    }

    public List<OrderTableIdRequest> getOrderTableIds() {
        return orderTableIds;
    }
}
