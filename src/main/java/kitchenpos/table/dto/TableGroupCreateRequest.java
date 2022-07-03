package kitchenpos.table.dto;

import java.util.ArrayList;
import java.util.List;

public class TableGroupCreateRequest {
    List<Long> orderTables = new ArrayList<>();

    protected TableGroupCreateRequest() {}

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTables.addAll(orderTableIds);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
