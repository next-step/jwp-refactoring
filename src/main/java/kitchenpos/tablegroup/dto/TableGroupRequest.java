package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
        // empty
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableRequest {
        private Long id;

        public OrderTableRequest() {
            // empty
        }

        public OrderTableRequest(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
