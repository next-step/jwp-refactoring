package kitchenpos.table.dto;

import java.util.List;
import java.util.Objects;
import kitchenpos.order.dto.OrderTableRequest;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroupRequest that = (TableGroupRequest) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
            "orderTables=" + orderTables +
            '}';
    }

}
