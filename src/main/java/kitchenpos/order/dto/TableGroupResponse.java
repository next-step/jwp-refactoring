package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {}

    public TableGroupResponse(TableGroup tableGroup, List<OrderTableResponse> orderTableResponses) {
        this.id = tableGroup.getId();
        this.orderTables = orderTableResponses;
    }

    public static TableGroupResponse of(TableGroup tableGroup, OrderTables orderTables) {
        List<OrderTableResponse> orderTableResponses = orderTables.getOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup, orderTableResponses);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
