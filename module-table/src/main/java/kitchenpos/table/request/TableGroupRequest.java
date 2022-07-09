package kitchenpos.table.request;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupRequest {

    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    protected TableGroupRequest() {
    }

    public List<OrderTable> getOrderTableIds() {
        return orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(createdDate, orderTables);
    }
}
