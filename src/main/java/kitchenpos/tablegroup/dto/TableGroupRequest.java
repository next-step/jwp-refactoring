package kitchenpos.tablegroup.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
