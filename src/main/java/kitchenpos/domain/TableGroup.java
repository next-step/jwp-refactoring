package kitchenpos.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    protected TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        this(null, orderTables);
    }

    public TableGroup(Long id, OrderTables orderTables) {
        this.id = id;
        this.orderTables = orderTables.withTableGroup(this);
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void ungroup() {
        orderTables.ungroup();
    }
}
