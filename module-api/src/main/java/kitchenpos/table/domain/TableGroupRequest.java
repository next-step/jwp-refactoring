package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.moduledomain.table.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;

public class TableGroupRequest {

    private List<Long> orderTables;

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public List<Long> validIsSizeEquals(OrderTables savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
