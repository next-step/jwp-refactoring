package kichenpos.table.group.ui.response;

import java.time.LocalDateTime;
import java.util.List;
import kichenpos.table.group.domain.TableGroup;
import kichenpos.table.table.ui.response.OrderTableResponse;

public final class TableGroupResponse {

    private long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    private TableGroupResponse() {
    }

    private TableGroupResponse(long id, LocalDateTime createdDate,
        List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(
            tableGroup.id(),
            tableGroup.createdDate(),
            OrderTableResponse.listFrom(tableGroup.orderTables())
        );
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
