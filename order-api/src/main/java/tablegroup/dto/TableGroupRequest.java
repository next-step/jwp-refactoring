package tablegroup.dto;

import ordertable.dto.OrderTableRequest;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {
    private Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableRequest> orderTables;

    private TableGroupRequest(Long id, LocalDateTime createdDate, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupRequest of(Long id, LocalDateTime createdDate, List<OrderTableRequest> orderTables) {
        return new TableGroupRequest(id, createdDate, orderTables);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
