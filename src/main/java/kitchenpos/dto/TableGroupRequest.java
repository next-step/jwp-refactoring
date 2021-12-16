package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private final List<Long> orderTables;

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream().map(OrderTable::getId).collect(Collectors.toList());
        return new TableGroupRequest(orderTableIds);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
