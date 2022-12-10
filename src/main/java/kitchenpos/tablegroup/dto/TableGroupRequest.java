package kitchenpos.tablegroup.dto;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTableIds;

    protected TableGroupRequest() {}

    private TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupRequest of(List<Long> orderTableIds) {
        return new TableGroupRequest(orderTableIds);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup createTableGroup(List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }
}
