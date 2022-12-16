package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.orderTables = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}

