package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableRespons;

    protected TableGroupResponse() {
    }

    private TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTableRespons) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableRespons = orderTableRespons;
    }

    public static TableGroupResponse of(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTableRespons) {
        return new TableGroupResponse(id, createdDate, orderTableRespons);
    }

    public static TableGroupResponse of(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        final List<OrderTableResponse> orderTableRespons = orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableRespons);
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTableRespons() {
        return orderTableRespons;
    }
}
