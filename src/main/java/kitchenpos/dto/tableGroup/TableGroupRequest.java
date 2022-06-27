package kitchenpos.dto.tableGroup;

import kitchenpos.domain.orderTable.OrderTables;
import kitchenpos.domain.tableGroup.TableGroup;

import java.util.List;

public class TableGroupRequest {
    private final List<Long> orderTableIds;

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroup toTableGroup(OrderTables orderTables) {
        return new TableGroup(null,  orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
