package kitchenpos.table.dto;

import kitchenpos.domain.TableGroup;

import java.util.List;

public class TableGroupResponse {
    private final long id;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(long id, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public static TableGroupResponse of(TableGroup save, List<OrderTableResponse> orderTableResponses) {
        return new TableGroupResponse(save.getId(), orderTableResponses);
    }
}
