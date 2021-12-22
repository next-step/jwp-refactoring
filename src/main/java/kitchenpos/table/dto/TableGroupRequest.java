package kitchenpos.table.dto;

import java.util.List;
import kitchenpos.table.domain.TableGroup;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

}
