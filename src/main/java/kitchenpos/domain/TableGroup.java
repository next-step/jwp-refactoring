package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void createdNow() {
        this.createdDate = LocalDateTime.now();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void initOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
