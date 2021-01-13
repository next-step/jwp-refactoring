package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.domain.OrderTable;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void updateOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
