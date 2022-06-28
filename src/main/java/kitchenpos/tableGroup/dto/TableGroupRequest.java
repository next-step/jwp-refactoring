package kitchenpos.tableGroup.dto;

import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {
    List<Long> orderTables;

    protected TableGroupRequest() {
    }

    private TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
