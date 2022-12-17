package kitchenpos.tablegroup.event;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;

public class TableGroupedEvent {

    private Long tableGroupId;
    private List<OrderTable> orderTables;

    public TableGroupedEvent(Long tableGroupId, List<OrderTable> orderTables) {
        this.tableGroupId = tableGroupId;
        this.orderTables = orderTables;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
