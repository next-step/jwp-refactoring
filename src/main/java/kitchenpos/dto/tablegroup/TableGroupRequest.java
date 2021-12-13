package kitchenpos.dto.tablegroup;

import java.util.List;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
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
