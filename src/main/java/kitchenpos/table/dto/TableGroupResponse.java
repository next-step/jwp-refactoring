package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

    private long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {
    }

    protected TableGroupResponse(long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables.getUnmodifiableOrderTables()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables());
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
