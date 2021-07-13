package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTableResponseList;

    public TableGroupResponse() {}

    public TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.orderTableResponseList = toOrderTableResponseList(tableGroup.getOrderTables());
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup);
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
