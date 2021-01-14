package dto.tablegroup;

import kitchenpos.domain.tablegroup.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableInTableGroupResponse> orderTables;

    TableGroupResponse() {
    }

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableInTableGroupResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        List<OrderTableInTableGroupResponse> orderTables = tableGroup.getOrderTables().stream()
                .map(it -> OrderTableInTableGroupResponse.of(it.getOrderTableId()))
                .collect(Collectors.toList());

        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableInTableGroupResponse> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate) && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, orderTables);
    }

    @Override
    public String toString() {
        return "TableGroupResponse{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", orderTables=" + orderTables +
                '}';
    }
}
