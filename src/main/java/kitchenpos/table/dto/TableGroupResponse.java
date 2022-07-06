package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.List;
import java.util.Objects;

public class TableGroupResponse {
    private final Long id;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(final Long id, final List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = OrderTableResponse.ofList(orderTables);
    }

    public static TableGroupResponse of (final TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getOrderTables());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    @Override
    public String toString() {
        return "TableGroupResponse{" +
                "id='" + id + '\'' +
                ", orderTables=" + orderTables +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTables);
    }
}
