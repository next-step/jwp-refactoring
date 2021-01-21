package kitchenpos.domain.tablegroup.dto;

import kitchenpos.domain.table.dto.OrderTableResponse;
import kitchenpos.domain.tablegroup.TableGroup;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(final Long id, final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public TableGroupResponse(final TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.orderTables = tableGroup.getOrderTables().stream()
            .map(OrderTableResponse::new)
            .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTableResponse> orderTables) {
        this.orderTables = orderTables;
    }
}
