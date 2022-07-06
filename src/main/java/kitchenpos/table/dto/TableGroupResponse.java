package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {
    }

    private TableGroupResponse(Long id, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        List<OrderTableResponse> orderTables = tableGroup.getOrderTables().stream()
            .map(orderTable -> OrderTableResponse.from(orderTable))
            .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
