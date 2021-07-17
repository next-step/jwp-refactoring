package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTableIdRequests;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableIdRequest> orderTableIdRequests) {
        this.orderTableIdRequests = orderTableIdRequests;
    }

    public List<OrderTableIdRequest> getOrderTableIdRequests() {
        return orderTableIdRequests;
    }

    public List<Long> ids() {
        return orderTableIdRequests.stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
    }

    public boolean isEmptyOrderTables() {
        return CollectionUtils.isEmpty(orderTableIdRequests);
    }

    public int orderTablesSize() {
        if (isEmptyOrderTables()) {
            return 0;
        }

        return orderTableIdRequests.size();
    }
}
