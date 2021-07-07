package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTable> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> ids) {
        orderTables = ids.stream()
                .map(OrderTable::new)
                .collect(Collectors.toList());
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(orderTable -> orderTable.id)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    class OrderTable {
        private Long id;

        public OrderTable() {
        }

        public OrderTable(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
