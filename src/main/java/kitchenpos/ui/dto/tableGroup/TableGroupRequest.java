package kitchenpos.ui.dto.tableGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableGroupRequest {
    private List<OrderTableInTableGroupRequest> orderTables;

    TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableInTableGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableInTableGroupRequest> getOrderTables() {
        return new ArrayList<>(this.orderTables);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroupRequest that = (TableGroupRequest) o;
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
