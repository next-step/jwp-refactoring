package kitchenpos.dto.table;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableIdsRequest> orderTableIdsRequests;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdsRequest> orderTableIdsRequests) {
        this.orderTableIdsRequests = orderTableIdsRequests;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIdsRequests.stream()
                                    .map(OrderTableIdsRequest::getId)
                                    .collect(Collectors.toList());
    }
}
