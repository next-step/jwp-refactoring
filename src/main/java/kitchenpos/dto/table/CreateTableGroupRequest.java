package kitchenpos.dto.table;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class CreateTableGroupRequest {

    private List<Long> orderTables;

    public CreateTableGroupRequest() {

    }

    public CreateTableGroupRequest(final List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup(final List<OrderTable> savedOrderTables) {
        return new TableGroup(savedOrderTables);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
