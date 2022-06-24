package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(
            orderTables.stream()
                .map(orderTableRequest ->
                    new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()))
                .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
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
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate)
            && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, orderTables);
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
            "id=" + id +
            ", createdDate=" + createdDate +
            ", orderTables=" + orderTables +
            '}';
    }

}
