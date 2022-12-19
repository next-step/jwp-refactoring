package kitchenpos.table.dto;

import java.util.Arrays;
import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTables;

    private TableGroupRequest() {}

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest(Long... orderTables) {
        this(Arrays.asList(orderTables));
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
