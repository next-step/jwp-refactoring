package kitchenpos.tobe.tablegroup.dto;

import kitchenpos.tobe.table.dto.OrderTableRequest;

import java.util.Collections;
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
        return Collections.unmodifiableList(orderTableRequests);
    }

    public List<Long> getOrderTableIds() {
        return orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
