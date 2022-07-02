package kitchenpos.service.tablegroup.dto;

import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.service.table.dto.OrderTableResponse;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse(TableGroup save) {
        this.id = save.getId();
        this.orderTables = save.getOrderTables().stream().map(OrderTableResponse::new).collect(Collectors.toList());
    }

    public TableGroupResponse() {
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
