package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.List;

public class TableGroupRequest {
    private List<Long> orderTables;

    public TableGroupRequest() {}

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroup toTableGroup(List<OrderTable> target) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.group(target);
        return tableGroup;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
