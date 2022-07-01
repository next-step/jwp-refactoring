package kitchenpos.table.dto;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {
    private final Long id;
    private final List<OrderTable> orderTables;

    private TableGroupResponse(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getOrderTables());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
