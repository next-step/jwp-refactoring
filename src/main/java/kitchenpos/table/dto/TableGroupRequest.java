package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private Long id;
    private List<TableRequest> orderTables;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setOrderTables(final List<TableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables.stream()
                .map(TableRequest::getId)
                .collect(Collectors.toList());
    }
}
