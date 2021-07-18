package kitchenpos.table.dto;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTableRequests;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public boolean isSameOrderTableCount(int orderTableSize) {
        return orderTableRequests.size() == orderTableSize;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
