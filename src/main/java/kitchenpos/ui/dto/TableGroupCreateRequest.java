package kitchenpos.ui.dto;

import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private List<OrderTableCreateRequest> orderTables;

    public TableGroup toEntity() {
        return new TableGroup(orderTables.stream()
                .map(OrderTableCreateRequest::toEntity)
                .collect(Collectors.toList()));
    }

    public List<OrderTableCreateRequest> getOrderTables() {
        return orderTables;
    }
}
