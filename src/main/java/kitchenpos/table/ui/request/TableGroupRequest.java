package kitchenpos.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private final List<OrderTableIdRequest> orderTables;

    @JsonCreator
    public TableGroupRequest(
        @JsonProperty("orderTables") List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
    }

    public static class OrderTableIdRequest {

        private final long id;

        @JsonCreator
        public OrderTableIdRequest(@JsonProperty("id") long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }
}
