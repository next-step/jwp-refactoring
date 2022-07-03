package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private Long id;

    private LocalDateTime createdDate;

    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
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