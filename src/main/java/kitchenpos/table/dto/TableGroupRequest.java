package kitchenpos.table.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private final List<OrderTable> list;

    @JsonCreator
    public TableGroupRequest(@JsonProperty("list") final List<OrderTable> list) {
        this.list = list;
    }

    public List<Long> getOrderTableIds () {
        return list.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getList() {
        return list;
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
                "list=" + list +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroupRequest that = (TableGroupRequest) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
