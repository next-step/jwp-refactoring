package kitchenpos.dto.request;

import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private Long id;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(Long id, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest of(TableGroup tableGroup) {
        List<OrderTableRequest> orderTableRequests = tableGroup.getOrderTables().stream()
                .map(OrderTableRequest::of)
                .collect(Collectors.toList());

        return new TableGroupRequest(tableGroup.getId(), orderTableRequests);
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
