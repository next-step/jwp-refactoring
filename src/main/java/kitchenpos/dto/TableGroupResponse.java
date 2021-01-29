package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private OrderTableResponses orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate,
        OrderTableResponses orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroupResponse, OrderTableResponses orderTables) {
        return new TableGroupResponse(tableGroupResponse.getId(), tableGroupResponse.getCreatedDate(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables.getList();
    }
}
