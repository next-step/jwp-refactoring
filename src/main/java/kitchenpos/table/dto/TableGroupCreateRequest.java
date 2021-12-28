package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroup toEntity(List<OrderTable> orderTables) {
        return TableGroup.of(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
