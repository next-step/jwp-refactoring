package kitchenpos.ui.dto.order;

import kitchenpos.domain.order.OrderTableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    protected OrderTableGroupResponse() {
    }

    public OrderTableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static OrderTableGroupResponse of(OrderTableGroup tableGroup) {
        return new OrderTableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList()));

    }

    public static List<OrderTableGroupResponse> ofList(List<OrderTableGroup> tableGroups) {
        return tableGroups.stream()
                .map(OrderTableGroupResponse::of)
                .collect(Collectors.toList());
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
