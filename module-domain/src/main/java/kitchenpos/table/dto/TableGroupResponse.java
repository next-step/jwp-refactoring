package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {}

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        List<OrderTableResponse> orderTables = tableGroup.getOrderTables().stream()
                .map(OrderTableResponse::from)
                .collect(toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
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
