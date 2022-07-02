package kitchenpos.service.tablegroup.dto;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse(TableGroup save) {
        this.id = save.getId();
        this.orderTables = save.getOrderTables().stream().map(OrderTableResponse::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
