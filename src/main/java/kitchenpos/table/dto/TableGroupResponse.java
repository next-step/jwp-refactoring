package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            OrderTableResponse.ofList(tableGroup.getOrderTables())
        );
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate,
        List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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
