package kitchenpos.order.dto;

import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables = new ArrayList<>();

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
