package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableId> orderTables;

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables.stream()
            .map(OrderTableId::new)
            .collect(Collectors.toList());
    }

    protected TableGroupRequest() {}

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTableId::getId)
            .collect(Collectors.toList());
    }

    static class OrderTableId {
        private Long id;

        protected OrderTableId() {
        }

        public OrderTableId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
