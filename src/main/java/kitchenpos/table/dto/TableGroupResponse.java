package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        Long id = tableGroup.getId();
        List<OrderTableResponse> orderTables = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
        return new TableGroupResponse(id, orderTables);
    }

    public TableGroup toTableGroup() {
        return new TableGroup(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderTables(List<OrderTableResponse> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
