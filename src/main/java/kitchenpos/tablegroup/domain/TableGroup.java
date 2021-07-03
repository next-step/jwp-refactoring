package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.table.domain.OrderTable;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {}

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
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

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
