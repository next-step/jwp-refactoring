package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.List;
import java.util.Objects;

public class TableGroupRequest {
    private final List<OrderTable> list;

    public TableGroupRequest(final List<OrderTable> list) {
        this.list = list;
    }

    public TableGroup toEntity() {
        return TableGroup.of(list);
    }

    public List<OrderTable> getList() {
        return list;
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
