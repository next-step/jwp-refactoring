package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableIdRequest> orderTables;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public TableGroupRequest(Long id, LocalDateTime createdDate, List<OrderTableIdRequest> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }
}
