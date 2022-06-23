package kitchenpos.dto.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroupRequest(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
