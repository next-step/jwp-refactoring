package kitchenpos.dto;

import javax.validation.constraints.Size;
import java.util.List;

public class TableGroupRequest {
    @Size(min = 2)
    private List<OrderTableId> orderTables;

    @SuppressWarnings("unused")
    protected TableGroupRequest() {}

    public TableGroupRequest(List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }
}
