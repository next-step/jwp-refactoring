package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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

    public TableGroup toTableGroup(List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
