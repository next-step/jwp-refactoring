package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {}

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroupResponse(TableGroup tableGroup, List<OrderTableResponse> orderTableResponses) {
        this.id = tableGroup.getId();
        this.orderTables = orderTableResponses;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTableResponse> orderTableResponses) {
        return new TableGroupResponse(tableGroup, orderTableResponses);
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
