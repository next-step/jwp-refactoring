package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {}

    private TableGroup(OrderTables orderTables, LocalDateTime createdDate) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        this.orderTables.toGroup(this);
    }

    public static TableGroup create(List<OrderTable> orderTables, LocalDateTime createdDate) {
        return create(OrderTables.of(orderTables), createdDate);
    }

    public static TableGroup create(OrderTables orderTables, LocalDateTime createdDate) {
        return new TableGroup(orderTables, createdDate);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.getOrderTableIds();
    }

    public void ungroup() {
        orderTables.ungrouped();
    }
}
