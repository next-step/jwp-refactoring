package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<TableGroupId> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<TableGroupId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables.stream().map(v -> v.getId()).collect(Collectors.toList());
    }
}
