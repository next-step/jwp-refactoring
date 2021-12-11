package kitchenpos.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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

    public TableGroup toEntity() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables.stream()
            .map(OrderTableIdRequest::toEntity)
            .collect(Collectors.toList()));
        return tableGroup;
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

        public OrderTable toEntity() {
            OrderTable orderTable = new OrderTable();
            orderTable.setId(id);
            return orderTable;
        }
    }
}
