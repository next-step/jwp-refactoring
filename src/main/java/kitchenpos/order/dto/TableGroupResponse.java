package kitchenpos.order.dto;

import kitchenpos.order.domain.TableGroup;

import java.util.List;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {}

    public TableGroupResponse(TableGroup tableGroup, List<OrderTableResponse> orderTableResponses) {
        this.id = tableGroup.getId();
        this.orderTables = orderTableResponses;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTableResponse> orderTableResponses) {
        return new TableGroupResponse(tableGroup, orderTableResponses);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
