package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.TableGroup;

public class TableGroupRequest {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTables = orderTableIds.stream()
            .map(OrderTableRequest::new)
            .collect(Collectors.toList());
    }

    public TableGroup toTableGroup() {
        return new TableGroup(
            id,
            createdDate,
            orderTables.stream()
                .map(OrderTableRequest::toOrderTable)
                .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

}
