package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupResponse {

    private Long id;
    private List<OrderTable> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(final Long id, final List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public static class OrderTable {
        private Long id;

        public OrderTable() {
        }

        public OrderTable(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
