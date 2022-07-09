package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.OrderTable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private final List<OrderTable> orderTables;

    @JsonCreator
    public TableGroupRequest(@JsonProperty("orderTables") final List<OrderTable> list) {
        this.orderTables = list;
    }

    public List<Long> fetchOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public int size() {
        return orderTables.size();
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
                "orderTables=" + orderTables +
                '}';
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
}
