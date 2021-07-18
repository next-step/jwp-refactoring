package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {

    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroupRequest(Long id, LocalDateTime createdDate, List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}