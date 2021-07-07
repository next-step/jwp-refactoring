package kitchenpos.tablegroup.dto;

import kitchenpos.ordertable.dto.OrderTableIdRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;

import java.util.List;

public class TableGroupRequest {
    List<OrderTableIdRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
