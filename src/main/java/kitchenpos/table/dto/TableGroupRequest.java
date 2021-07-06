package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(LocalDateTime createdDate, List<OrderTableRequest> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
