package kitchenpos.table.ui.dto;

import kitchenpos.table.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private List<OrderTableCreateRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(orderTables.stream()
                .map(OrderTableCreateRequest::toEntity)
                .collect(Collectors.toList()));
    }

    public List<OrderTableCreateRequest> getOrderTables() {
        return orderTables;
    }
}
