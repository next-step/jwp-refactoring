package kitchenpos.tablegroup.dto;

import java.util.List;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.utils.StreamUtils;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    protected TableGroupRequest() {}

    private TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(List<OrderTableIdRequest> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toTableGroup() {
        return TableGroup.from(StreamUtils.mapToList(orderTables, request -> OrderTable.from(request.getId())));
    }
}
