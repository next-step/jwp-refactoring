package kitchenpos.tablegroup.dto;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

public class TableGroupCreateRequest {

    @Valid
    @Size(min = 2)
    private List<OrderTable> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
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