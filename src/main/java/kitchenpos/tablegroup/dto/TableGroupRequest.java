package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.OrderTableRequest;

import java.util.ArrayList;
import java.util.List;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
        this.orderTables = new ArrayList<>();
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return this.orderTables;
    }
}
