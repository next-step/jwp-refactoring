package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> ids() {
        return orderTables.stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
    }

    public boolean isEmptyOrderTables() {
        return CollectionUtils.isEmpty(orderTables);
    }

    public int orderTablesSize() {
        if (isEmptyOrderTables()) {
            return 0;
        }

        return orderTables.size();
    }
}
