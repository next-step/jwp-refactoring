package kitchenpos.table.dto;


import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    protected TableGroupResponse() {
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                                      OrderTableResponse.of(tableGroup.getOrderTables()));

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
