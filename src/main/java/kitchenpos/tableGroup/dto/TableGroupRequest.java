package kitchenpos.tableGroup.dto;

import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTableRequests;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }

    public List<Long> toOrderTableIds() {
        return orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public int orderTablesSize() {
        return this.orderTableRequests.size();
    }

}
