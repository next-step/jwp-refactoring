package kitchenpos.order.dto;

import kitchenpos.order.domain.TableGroup;

import java.util.List;

public class TableGroupRequest {
    private Long id;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {}

    public TableGroupRequest(Long id, List<OrderTableRequest> orderTableRequests) {
        this.id = id;
        this.orderTables = orderTableRequests;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
