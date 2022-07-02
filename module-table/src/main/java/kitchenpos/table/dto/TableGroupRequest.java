package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private final Long id;
    private final List<OrderTableRequest> orderTables;

    public TableGroupRequest(Long id, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getRequestOrderTableIds(){
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

}
