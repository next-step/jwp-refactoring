package kitchenpos.tablegroup.dto;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdAt;
    private final List<OrderTableResponse> orderTables;

    public static TableGroupResponse from(TableGroup tableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedAt(), convertOrderTableResponse(orderTables));
    }

    private TableGroupResponse(Long id, LocalDateTime createdAt, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    private static List<OrderTableResponse> convertOrderTableResponse(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt) && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, orderTables);
    }

    @Override
    public String toString() {
        return "TableGroupResponse{" +
                "id=" + id +
                ", createdDate=" + createdAt +
                ", orderTables=" + orderTables +
                '}';
    }
}
