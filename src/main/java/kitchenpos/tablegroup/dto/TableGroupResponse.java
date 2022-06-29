package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(TableGroup tableGroup) {
        this.orderTables = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }
}
