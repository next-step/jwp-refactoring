package kitchenpos.tablegroup.event;

import kitchenpos.ordertable.dto.OrderTableIdRequest;

import java.util.List;

public class TableGroupCreatedEvent {
    List<OrderTableIdRequest> orderTableIdRequests;

    public TableGroupCreatedEvent(List<OrderTableIdRequest> orderTableIdRequests) {
        this.orderTableIdRequests = orderTableIdRequests;
    }

    public List<OrderTableIdRequest> getOrderTableIdRequests() {
        return orderTableIdRequests;
    }
}
