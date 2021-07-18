package kitchenpos.tablegroup.event;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;

public class TableGroupUpdateEvent {
    private final TableGroup tableGroup;
    private final List<OrderTableIdRequest> orderTableIdRequests;

    public TableGroupUpdateEvent(final TableGroup tableGroup, final List<OrderTableIdRequest> orderTableIdRequests) {
        this.tableGroup = tableGroup;
        this.orderTableIdRequests = orderTableIdRequests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public List<OrderTableIdRequest> getOrderTableIdRequests() {
        return orderTableIdRequests;
    }

    public List<Long> getOrderTablesIds() {
        return orderTableIdRequests.stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
    }
}
