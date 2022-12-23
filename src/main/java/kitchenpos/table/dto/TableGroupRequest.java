package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<Long> orderTableIds;

    public TableGroupRequest() {

    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupRequest of(TableGroup tableGroup) {
        return new TableGroupRequest(tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList()));
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
