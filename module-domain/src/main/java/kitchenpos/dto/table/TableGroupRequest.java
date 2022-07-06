package kitchenpos.dto.table;

import java.util.ArrayList;
import java.util.List;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables = new ArrayList<>();

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
