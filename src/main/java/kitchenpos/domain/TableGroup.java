package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
    }
    private void validateOrderTables(OrderTables orderTables) {
        if (isNotSatisfyToTableGroup(orderTables)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNotSatisfyToTableGroup(OrderTables orderTables) {
        return orderTables.hasOccupiedTable() || orderTables.hasTableGroupId();
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
        return orderTables.toList();
    }

    public void setOrderTables(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }
}
