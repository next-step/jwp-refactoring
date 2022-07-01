package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());

        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(getOrderTables(), that.getOrderTables());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderTables());
    }
}
