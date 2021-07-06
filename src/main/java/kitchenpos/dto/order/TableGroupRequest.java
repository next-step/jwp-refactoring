package kitchenpos.dto.order;

import java.util.List;
import java.util.Objects;

public class TableGroupRequest {

    private final List<OrderTableId> orderTables;

    public TableGroupRequest(List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableId> getOrderTableRequests() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
