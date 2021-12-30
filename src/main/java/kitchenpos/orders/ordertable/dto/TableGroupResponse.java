package kitchenpos.orders.ordertable.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.orders.ordertable.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;

    private final LocalDateTime createdDate;

    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(
        final Long id,
        final LocalDateTime createdDate,
        final List<OrderTableResponse> orderTables
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(final TableGroup group) {
        return new TableGroupResponse(
            group.getId(),
            group.getCreatedDate(),
            group.getOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList())
        );
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
}
