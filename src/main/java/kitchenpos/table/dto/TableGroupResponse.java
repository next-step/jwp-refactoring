package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(TableGroup tableGroup, List<OrderTable> orderTables) {
        this.id = tableGroup.getId();
        this.orderTables = orderTables
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup, Collections.EMPTY_LIST);
    }
    public static TableGroupResponse of(TableGroup tableGroup, OrderTables orderTables) {
        return new TableGroupResponse(tableGroup,orderTables.getOrderTables());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}

