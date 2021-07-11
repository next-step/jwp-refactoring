package kitchenpos.table.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    protected OrderTableGroupRequest() {
    }

    public OrderTableGroupRequest(Long id, LocalDateTime createdDate, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static OrderTableGroupRequest of(LocalDateTime createdDate, List<OrderTableRequest> orderTables) {
        return new OrderTableGroupRequest(null, createdDate, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
