package kitchenpos.dto;

import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableResponses;

    public TableGroupResponse() {

    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse of(TableGroup tableGroup, OrderTables orderTables) {
        List<OrderTableResponse> orderTableResponses = orderTables.getOrderTables().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
