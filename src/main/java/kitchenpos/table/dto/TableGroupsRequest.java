package kitchenpos.table.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public class TableGroupsRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupsRequest() {
    }

    public TableGroupsRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<OrderTable> toEntity() {
        return this.orderTables.stream()
            .map(orderTable -> OrderTable.of(orderTable.id))
            .collect(toList());
    }

    public static class OrderTableRequest {

        private Long id;

        public OrderTableRequest() {
        }

        public OrderTableRequest(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
