package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTables;

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableRequest {

        private Long id;

        public Long getId() {
            return id;
        }
    }
}
