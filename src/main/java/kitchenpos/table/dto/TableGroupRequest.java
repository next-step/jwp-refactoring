package kitchenpos.table.dto;

import kitchenpos.common.exception.NotFoundOrderTableException;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup toTableGroup(OrderTables orderTables) {
        if (isNotFoundOrderTables(orderTables)) {
            throw new NotFoundOrderTableException();
        }

        return new TableGroup(orderTables);
    }

    private boolean isNotFoundOrderTables(OrderTables orderTables) {
        return orderTableIds.size() != orderTables.size();
    }
}
