package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

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
