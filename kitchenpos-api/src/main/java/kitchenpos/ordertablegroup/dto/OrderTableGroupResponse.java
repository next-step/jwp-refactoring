package kitchenpos.ordertablegroup.dto;

import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;

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

    public static OrderTableGroupResponse of(OrderTables orderTables) {
        OrderTableGroup orderTableGroup = orderTables.getOrderTables().get(0).getOrderTableGroup();
        return new OrderTableGroupResponse(orderTableGroup.getId(), orderTableGroup.getCreatedDate(),
            orderTables.getOrderTables().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList()));
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
