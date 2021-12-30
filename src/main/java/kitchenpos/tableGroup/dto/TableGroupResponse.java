package kitchenpos.tableGroup.dto;

import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.tableGroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTableIds;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTableResponse> orderTables) {
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
