package kitchenpos.tablegroup.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TableGroupRequest {

    private static final int ORDER_TABLE_MIN_SIZE = 2;

    @NotEmpty
    @Size(min = ORDER_TABLE_MIN_SIZE)
    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest() {
        // empty
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableIdRequest {
        private Long id;

        public OrderTableIdRequest() {
            // empty
        }

        public OrderTableIdRequest(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
