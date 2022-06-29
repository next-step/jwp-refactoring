package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createDate;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {
    }

    protected TableGroupResponse(Long id, LocalDateTime createDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createDate = createDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), OrderTableResponse.from(tableGroup.getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
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
        TableGroupResponse response = (TableGroupResponse) o;
        return Objects.equals(id, response.id) && Objects.equals(createDate, response.createDate)
                && Objects.equals(orderTables, response.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, orderTables);
    }
}
