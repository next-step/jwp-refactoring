package kitchenpos.dto.tablegroup;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;


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

    public OrderTables toEntity() {
        return OrderTables.of(this.orderTables.stream()
            .map(orderTable -> OrderTable.of(orderTable.id))
            .collect(toList()));
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
