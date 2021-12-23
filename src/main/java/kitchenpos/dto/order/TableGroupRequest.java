package kitchenpos.dto.order;

import java.util.List;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;

public class TableGroupRequest {

    List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup toTableGroup(List<OrderTable> orderTables) {
        return TableGroup.of(orderTables);
    }

    public int getOrderTableSize() {
        return orderTableIds.size();
    }
}
