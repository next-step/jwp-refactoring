package kitchenpos.tablegroup.dto;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

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
