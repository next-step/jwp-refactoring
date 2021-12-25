package kitchenpos.ordertable.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    private TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate,
        List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate()
            , tableGroup.getOrderTableList().stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList()));
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(
            getCreatedDate(), that.getCreatedDate()) && Objects.equals(getOrderTables(),
            that.getOrderTables());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedDate(), getOrderTables());
    }
}
