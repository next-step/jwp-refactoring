package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdTime;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdTime, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdTime = createdTime;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup){
        List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                orderTableResponses);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
