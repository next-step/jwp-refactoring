package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTables = orderTableIds.stream()
            .map(OrderTableRequest::new)
            .collect(Collectors.toList());
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList());
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableRequest {
        private Long id;

        public OrderTableRequest() {
        }

        public OrderTableRequest(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
