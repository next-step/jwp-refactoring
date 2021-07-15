package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTableResponseList;

    public TableGroupResponse() {}

    public TableGroupResponse(TableGroup tableGroup, List<OrderTable> orderTables) {
        this.id = tableGroup.getId();
        this.orderTableResponseList = toOrderTableResponseList(orderTables);
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(tableGroup, orderTables);
    }

    public List<OrderTableResponse> toOrderTableResponseList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTableResponseList() {
        return orderTableResponseList;
    }
}
