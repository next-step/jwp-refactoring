package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupRequest {
    private List<Long> orderTables;

    protected TableGroupRequest() {}

    private TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }

    public TableGroup toTableGroup() {
        return TableGroup.from(
                this.orderTables.stream()
                        .map(OrderTable::from)
                        .collect(Collectors.toList())
        );
    }
}
