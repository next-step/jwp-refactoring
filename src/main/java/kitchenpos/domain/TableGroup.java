package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(Long id, LocalDateTime createdDate, OrderTable... orderTables) {
        return new TableGroup(id, createdDate, Arrays.asList(orderTables));
    }

    public static TableGroup of(Long id, LocalDateTime createdDate) {
        return new TableGroup(id, createdDate, null);
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
