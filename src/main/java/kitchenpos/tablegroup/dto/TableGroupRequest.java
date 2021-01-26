package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private Long id;
    private List<OrderTableRequest> orderTableRequests;

    public TableGroupRequest() {
    }

    public TableGroupRequest(Long id, List<OrderTableRequest> orderTableRequests) {
        this.id = id;
        this.orderTableRequests = orderTableRequests;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }

    public List<Long> getOrderTableIds() {
        return orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
